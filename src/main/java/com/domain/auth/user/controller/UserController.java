package com.domain.auth.user.controller;

import com.domain.auth.user.projection.UserInfo;
import com.domain.auth.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @GetMapping("/user-info/{email}")
    public ResponseEntity<UserInfo> getUserInfo(@PathVariable String email) {
        return ResponseEntity.ok(service.getUserInfo(email));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserInfo> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @GetMapping("/ids")
    public ResponseEntity<List<UserInfo>> getUserByIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(service.getUsersByIds(ids));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserInfo>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }
}
