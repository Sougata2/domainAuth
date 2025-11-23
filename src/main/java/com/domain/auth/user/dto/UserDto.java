package com.domain.auth.user.dto;

import com.domain.auth.role.entity.RoleEntity;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link com.domain.auth.user.entity.UserEntity}
 */
@Builder
public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String password,
        Set<RoleEntity> roles,
        RoleEntity defaultRole,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {
}