package com.domain.auth.auth.service.impl;

import com.domain.auth.auth.dto.AuthDto;
import com.domain.auth.auth.dto.LoginDto;
import com.domain.auth.auth.dto.RegisterDto;
import com.domain.auth.auth.service.AuthService;
import com.domain.auth.jwt.properties.JwtProperties;
import com.domain.auth.jwt.service.JwtService;
import com.domain.auth.user.entity.UserEntity;
import com.domain.auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;
    private final JwtService jwtService;

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
}
