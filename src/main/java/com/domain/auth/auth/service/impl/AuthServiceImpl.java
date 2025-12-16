package com.domain.auth.auth.service.impl;

import com.domain.auth.auth.dto.AuthDto;
import com.domain.auth.auth.dto.LoginDto;
import com.domain.auth.auth.dto.RegisterDto;
import com.domain.auth.auth.service.AuthService;
import com.domain.auth.jwt.properties.JwtProperties;
import com.domain.auth.jwt.service.JwtService;
import com.domain.auth.user.dto.UserDto;
import com.domain.auth.user.entity.UserEntity;
import com.domain.auth.user.repository.UserRepository;
import com.domain.mapper.service.MapperService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;
    private final JwtService jwtService;
    private final MapperService mapper;

    @Override
    public UserDetails authenticate(String username, String password) throws AuthenticationException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return userDetailsService.loadUserByUsername(username);
    }

    @Override
    public AuthDto login(LoginDto loginDto) {
        try {
            UserDetails user = authenticate(loginDto.email(), loginDto.password());
            String token = jwtService.generateToken(user.getUsername());
            return new AuthDto(user.getUsername(), token, LocalDateTime.now().plusSeconds(jwtProperties.getExpiration() / 1000));
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

    private String extractToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
