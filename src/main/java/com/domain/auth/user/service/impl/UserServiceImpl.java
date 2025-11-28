package com.domain.auth.user.service.impl;

import com.domain.auth.user.dto.UserDto;
import com.domain.auth.user.entity.UserEntity;
import com.domain.auth.user.repository.UserRepository;
import com.domain.auth.user.service.UserService;
import com.domain.mapper.service.MapperService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final MapperService mapper;

    @Override
    public UserDto findByEmail(String email) {
        try {
            Optional<UserEntity> entity = repository.findByEmail(email);
            if (entity.isEmpty()) {
                throw new EntityNotFoundException("User with email %s not found".formatted(email));
            }
            return (UserDto) mapper.toDto(entity.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
