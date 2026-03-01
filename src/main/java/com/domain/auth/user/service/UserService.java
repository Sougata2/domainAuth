package com.domain.auth.user.service;

import com.domain.auth.user.dto.UserDto;
import com.domain.auth.user.projection.UserInfo;


public interface UserService {
    UserDto findByEmail(String email);

    UserInfo getUserInfo(String email);
}
