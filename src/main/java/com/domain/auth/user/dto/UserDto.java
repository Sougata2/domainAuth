package com.domain.auth.user.dto;

import com.domain.auth.role.dto.RoleDto;
import com.domain.auth.role.entity.RoleEntity;
import com.domain.mapper.references.MasterDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link com.domain.auth.user.entity.UserEntity}
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable, MasterDto {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        @JsonIgnore
        private String password;
        private Set<RoleDto> roles;
        private RoleDto defaultRole;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
}