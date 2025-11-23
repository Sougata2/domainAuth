package com.domain.auth.role.service.impl;

import com.domain.auth.role.dto.RoleDto;
import com.domain.auth.role.entity.RoleEntity;
import com.domain.auth.role.repository.RoleRepository;
import com.domain.auth.role.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;

    @Override
    public List<RoleDto> findAll() {
        try {
            List<RoleEntity> entities = repository.findAll();
            return entities.stream().map(e -> RoleDto.builder()
                    .id(e.getId())
                    .name(e.getName())
                    .defaultRoleUsers(e.getDefaultRoleUsers())
                    .users(e.getUsers())
                    .createdAt(e.getCreatedAt())
                    .updatedAt(e.getUpdatedAt())
                    .build()
            ).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RoleDto findById(Long id) {
        try {
            Optional<RoleEntity> entity = repository.findById(id);
            if (entity.isEmpty()) {
                throw new EntityNotFoundException("Role with id %d not found".formatted(id));
            }
            return RoleDto.builder()
                    .id(entity.get().getId())
                    .name(entity.get().getName())
                    .defaultRoleUsers(entity.get().getDefaultRoleUsers())
                    .users(entity.get().getUsers())
                    .createdAt(entity.get().getCreatedAt())
                    .updatedAt(entity.get().getUpdatedAt())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RoleDto findByName(String name) {
        try {
            Optional<RoleEntity> entity = repository.findByName(name);
            if (entity.isEmpty()) {
                throw new EntityNotFoundException("Role with name %s not found".formatted(name));
            }
            return RoleDto.builder()
                    .id(entity.get().getId())
                    .name(entity.get().getName())
                    .defaultRoleUsers(entity.get().getDefaultRoleUsers())
                    .users(entity.get().getUsers())
                    .createdAt(entity.get().getCreatedAt())
                    .updatedAt(entity.get().getUpdatedAt())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public RoleDto create(RoleDto dto) {
        try {
            RoleEntity entity = RoleEntity.builder().name(dto.name()).build();
            RoleEntity saved = repository.save(entity);
            return RoleDto.builder()
                    .id(saved.getId())
                    .name(saved.getName())
                    .defaultRoleUsers(saved.getDefaultRoleUsers())
                    .users(saved.getUsers())
                    .createdAt(saved.getCreatedAt())
                    .updatedAt(saved.getUpdatedAt())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RoleDto update(RoleDto dto) {
        return null;
    }

    @Override
    public RoleDto delete(RoleDto dto) {
        try {
            Optional<RoleEntity> entity = repository.findById(dto.id());
            if (entity.isEmpty()) {
                throw new EntityNotFoundException("Role with id %d not found".formatted(dto.id()));
            }

            repository.delete(entity.get());
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
