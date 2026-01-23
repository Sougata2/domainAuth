package com.domain.auth.auth.service.impl;

import com.domain.auth.appUser.details.AppUserDetails;
import com.domain.auth.auth.dto.AuthDto;
import com.domain.auth.auth.dto.LoginDto;
import com.domain.auth.auth.dto.RefreshTokenDto;
import com.domain.auth.auth.dto.RegisterDto;
import com.domain.auth.auth.entity.RefreshTokenEntity;
import com.domain.auth.auth.properties.AuthTokenProperties;
import com.domain.auth.auth.repository.RefreshTokenRepository;
import com.domain.auth.auth.service.AuthService;
import com.domain.auth.jwt.service.JwtService;
import com.domain.auth.role.entity.RoleEntity;
import com.domain.auth.role.repository.RoleRepository;
import com.domain.auth.user.dto.UserDto;
import com.domain.auth.user.entity.UserEntity;
import com.domain.auth.user.repository.UserRepository;
import com.domain.mapper.service.MapperService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final AuthTokenProperties tokenProperties;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final MapperService mapper;

    @Override
    public UserDetails authenticate(String username, String password) throws AuthenticationException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return userDetailsService.loadUserByUsername(username);
    }

    @Override
    public AuthDto login(LoginDto loginDto, HttpServletResponse response) {
        try {
            UserEntity user = ((AppUserDetails) authenticate(loginDto.email(), loginDto.password())).getEntity();
            String token = jwtService.generateToken(user.getEmail());
            UUID refreshToken = createRefreshToken(user, loginDto.device()).getToken();

            /*
            HttpOnly → safe from XSS
            Secure → HTTPS only
            SameSite Strict → CSRF safe
            */
            ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken.toString())
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .path("auth-service/auth/refresh")
                    .maxAge(tokenProperties.getRefreshExpiration() / 1000)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return new AuthDto(
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    token,
                    LocalDateTime.now().plusSeconds(tokenProperties.getAccessExpiration() / 1000)
            );
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Bad credentials");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void register(RegisterDto dto) {
        try {
            UserEntity entity = UserEntity.builder()
                    .firstName(dto.firstName())
                    .lastName(dto.lastName())
                    .email(dto.email())
                    .password(dto.password())
                    .build();
            userRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDto validate(AuthDto dto) {
        String username = extractToken(dto.token());
        UserEntity user = userRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("User %s is not found".formatted(username)));
        return (UserDto) mapper.toDto(user);
    }

    @Override
    @Transactional
    public void registerWithDefaultRole(RegisterDto dto, String roleName) {
        RoleEntity role = roleRepository.findByName(roleName).orElseThrow(() -> new EntityNotFoundException("Role %s is not found".formatted(roleName)));
        UserEntity entity = UserEntity.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .password(dto.password())
                .roles(new HashSet<>(Set.of(role)))
                .defaultRole(role)
                .build();
        userRepository.save(entity);
    }

    @Override
    @Transactional
    public AuthDto refresh(UUID refreshToken, HttpServletResponse response) {
        RefreshTokenEntity tokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
        if (tokenEntity.isRevoked() || tokenEntity.getExpiresAt().isBefore(Instant.now())) {
            tokenEntity.setRevoked(true);
            refreshTokenRepository.save(tokenEntity);
            throw new BadCredentialsException("Refresh token is expired");
        }

        // rotate refresh token
        tokenEntity.setRevoked(true);
        refreshTokenRepository.save(tokenEntity);

        RefreshTokenEntity newToken = refreshTokenRepository.save(
                RefreshTokenEntity.builder()
                        .user(tokenEntity.getUser())
                        .token(UUID.randomUUID())
                        .expiresAt(Instant.now().plusMillis(tokenProperties.getRefreshExpiration()))
                        .revoked(false)
                        .device(tokenEntity.getDevice())
                        .build()
        );

        ResponseCookie cookie = ResponseCookie.from("refresh_token", newToken.getToken().toString())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("auth-service/auth/refresh")
                .maxAge(tokenProperties.getRefreshExpiration() / 1000)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        String accessToken = jwtService.generateToken(tokenEntity.getUser().getEmail());
        return new AuthDto(
                tokenEntity.getUser().getEmail(),
                tokenEntity.getUser().getFirstName(),
                tokenEntity.getUser().getLastName(),
                accessToken,
                LocalDateTime.now().plusSeconds(tokenProperties.getAccessExpiration() / 1000)
        );
    }

    @Override
    @Transactional
    public void logout(UUID refreshToken, HttpServletResponse response) {
        if (refreshToken != null) {
            refreshTokenRepository.findByToken(refreshToken)
                    .ifPresent((tokenEntity) -> {
                        tokenEntity.setRevoked(true);
                        refreshTokenRepository.save(tokenEntity);
                    });
        }

        /*
         HttpOnly → safe from XSS
         Secure → HTTPS only
         SameSite Strict → CSRF safe
        */
        ResponseCookie delete = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("auth-service/auth/refresh")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, delete.toString());
    }

    private RefreshTokenDto createRefreshToken(UserEntity user, String device) {
        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .user(user)
                .token(UUID.randomUUID())
                .expiresAt(Instant.now().plusMillis(tokenProperties.getRefreshExpiration()))
                .revoked(false)
                .device(device)
                .build();
        RefreshTokenEntity saved = refreshTokenRepository.save(refreshToken);
        return (RefreshTokenDto) mapper.toDto(saved);
    }

    private String extractToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(tokenProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
