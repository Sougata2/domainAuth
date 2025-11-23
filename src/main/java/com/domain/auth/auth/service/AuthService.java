package com.domain.auth.auth.service;

import com.domain.auth.auth.dto.AuthDto;
import com.domain.auth.auth.dto.LoginDto;
import com.domain.auth.auth.dto.RegisterDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    UserDetails authenticate(String username, String password);

    AuthDto login(LoginDto dto);

    void register(RegisterDto dto);
}
