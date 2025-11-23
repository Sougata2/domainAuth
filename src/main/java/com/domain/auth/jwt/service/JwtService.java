package com.domain.auth.jwt.service;

public interface JwtService {
    String generateToken(String username, String role);
}
