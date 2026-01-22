package com.domain.auth.auth.dto;

import java.time.LocalDateTime;

public record AuthDto(
        String email,
        String firstName,
        String lastName,
        String token,
        LocalDateTime expiration
) {
}
