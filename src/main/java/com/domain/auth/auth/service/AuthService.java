package com.domain.auth.auth.service;

import com.domain.auth.auth.dto.AuthDto;
import com.domain.auth.auth.dto.LoginDto;
import com.domain.auth.auth.dto.RegisterDto;
import com.domain.auth.auth.dto.ResetPasswordDto;
import com.domain.auth.user.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface AuthService {
    UserDetails authenticate(String username, String password);

    AuthDto login(LoginDto dto, HttpServletResponse response);

    void register(RegisterDto dto);

    UserDto validate(AuthDto dto);

    void registerWithDefaultRole(RegisterDto dto, String roleName);

    AuthDto refresh(UUID refreshToken, HttpServletResponse response);

    void logout(UUID refreshToken, HttpServletResponse response);

    void resetPassword(ResetPasswordDto dto);
}
