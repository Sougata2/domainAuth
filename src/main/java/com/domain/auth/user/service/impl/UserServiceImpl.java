package com.domain.auth.user.service.impl;

import com.domain.auth.user.dto.UserDto;
import com.domain.auth.user.entity.UserEntity;
import com.domain.auth.user.repository.UserRepository;
import com.domain.auth.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto findByEmail(String email) {
        try {
            Optional<UserEntity> entity = repository.findByEmail(email);
            if (entity.isEmpty()) {
                throw new EntityNotFoundException("User with email %s not found".formatted(email));
            }
            return UserDto.builder()
                    .id(entity.get().getId())
                    .firstName(entity.get().getFirstName())
                    .lastName(entity.get().getLastName())
                    .email(entity.get().getEmail())
                    .roles(entity.get().getRoles())
                    .defaultRole(entity.get().getDefaultRole())
                    .createdAt(entity.get().getCreatedAt())
                    .updatedAt(entity.get().getUpdatedAt())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
