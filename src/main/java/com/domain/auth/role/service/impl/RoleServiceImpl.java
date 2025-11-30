package com.domain.auth.role.service.impl;

import com.domain.auth.role.dto.RoleDto;
import com.domain.auth.role.entity.RoleEntity;
import com.domain.auth.role.repository.RoleRepository;
import com.domain.auth.role.service.RoleService;
import com.domain.mapper.service.MapperService;
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
    private final MapperService mapper;

    @Override
    public List<RoleDto> findAll() {
        try {
            List<RoleEntity> entities = repository.findAll();
            return entities.stream().map(e -> (RoleDto) mapper.toDto(e)).toList();
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
            return (RoleDto) mapper.toDto(entity.get());
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

            return (RoleDto) mapper.toDto(entity.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public RoleDto create(RoleDto dto) {
        try {
            RoleEntity entity = (RoleEntity) mapper.toEntity(dto);
            RoleEntity saved = repository.save(entity);
            return (RoleDto) mapper.toDto(saved);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RoleDto update(RoleDto dto) {
        try {
            Optional<RoleEntity> og = repository.findById(dto.getId());
            if (og.isEmpty()) {
                throw new EntityNotFoundException("Role with id %d not found".formatted(dto.getId()));
            }
            RoleEntity nu = (RoleEntity) mapper.toEntity(dto);
            RoleEntity merged = (RoleEntity) mapper.merge(og.get(), nu);
            RoleEntity saved = repository.save(merged);
            return (RoleDto) mapper.toDto(saved);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RoleDto delete(RoleDto dto) {
        try {
            Optional<RoleEntity> entity = repository.findById(dto.getId());
            if (entity.isEmpty()) {
                throw new EntityNotFoundException("Role with id %d not found".formatted(dto.getId()));
            }

            repository.delete(entity.get());
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
