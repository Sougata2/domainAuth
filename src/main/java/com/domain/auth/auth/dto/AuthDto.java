package com.domain.auth.auth.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuthDto(String email, String token, UUID refreshToken, LocalDateTime expiration) {
}
