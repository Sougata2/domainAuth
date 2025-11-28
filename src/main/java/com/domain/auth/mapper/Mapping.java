package com.domain.auth.mapper;

import com.domain.auth.role.dto.RoleDto;
import com.domain.auth.role.entity.RoleEntity;
import com.domain.auth.user.dto.UserDto;
import com.domain.auth.user.entity.UserEntity;
import com.domain.mapper.references.MasterDto;
import com.domain.mapper.references.MasterEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Mapping implements com.domain.mapper.mapping.Mapping {
    @Override
    public Map<Class<? extends MasterEntity>, Class<? extends MasterDto>> getEntityDtoMap() {
        return Map.ofEntries(
                Map.entry(UserEntity.class, UserDto.class),
                Map.entry(RoleEntity.class, RoleDto.class)
        );
    }

    @Override
    public Map<Class<? extends MasterDto>, Class<? extends MasterEntity>> getDtoEntityMap() {
        return Map.ofEntries(
                Map.entry(UserDto.class, UserEntity.class),
                Map.entry(RoleDto.class, RoleEntity.class)
        );
    }
}
