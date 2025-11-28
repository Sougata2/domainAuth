package com.domain.auth.role.dto;

import com.domain.auth.user.dto.UserDto;
import com.domain.auth.user.entity.UserEntity;
import com.domain.mapper.references.MasterDto;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link com.domain.auth.role.entity.RoleEntity}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto implements Serializable, MasterDto {
        private Long id;
        private String name;
        private Set<UserDto> users;
        private Set<UserDto> defaultRoleUsers;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
}