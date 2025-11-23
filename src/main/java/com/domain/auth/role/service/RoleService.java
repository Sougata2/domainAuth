package com.domain.auth.role.service;

import com.domain.auth.role.dto.RoleDto;

import java.util.List;

public interface RoleService {
    List<RoleDto> findAll();

    RoleDto findById(Long id);

    RoleDto findByName(String name);

    RoleDto create(RoleDto dto);

    RoleDto update(RoleDto dto);

    RoleDto delete(RoleDto dto);
}
