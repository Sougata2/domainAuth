package com.domain.auth.user.controller;

import com.domain.auth.user.projection.UserInfo;
import com.domain.auth.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @GetMapping("/user-info/{email}")
    public ResponseEntity<UserInfo> getUserInfo(@PathVariable String email) {
        return ResponseEntity.ok(service.getUserInfo(email));
    }
}
