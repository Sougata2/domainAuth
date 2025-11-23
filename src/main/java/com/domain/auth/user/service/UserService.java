package com.domain.auth.user.service;

import com.domain.auth.user.dto.UserDto;


public interface UserService {
    UserDto findByEmail(String email);
}
