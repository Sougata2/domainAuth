package com.domain.auth.auth.dto;

import com.domain.auth.user.entity.UserEntity;
import com.domain.mapper.references.MasterDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.domain.auth.auth.entity.RefreshTokenEntity}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDto implements Serializable, MasterDto {
    private Long id;
    private UserEntity user;
    private UUID token;
    private Instant expiresAt;
    private boolean revoked;
    private String device;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}