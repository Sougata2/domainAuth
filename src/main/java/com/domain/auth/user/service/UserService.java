package com.domain.auth.user.service;

import com.domain.auth.user.dto.UserDto;
import com.domain.auth.user.projection.UserInfo;

import java.util.List;


public interface UserService {
    UserDto findByEmail(String email);

    UserInfo getUserInfo(String email);

    UserInfo getUserById(Long id);

    List<UserInfo> getAllUsers();
}
