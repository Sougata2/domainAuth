package com.domain.auth.role.dto;

import com.domain.auth.user.entity.UserEntity;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link com.domain.auth.role.entity.RoleEntity}
 */
@Builder
public record RoleDto(
        Long id,
        String name,
        Set<UserEntity> users,
        Set<UserEntity> defaultRoleUsers,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {
}