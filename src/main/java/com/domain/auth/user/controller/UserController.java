package com.domain.auth.user.controller;

import com.domain.auth.user.dto.UserDto;
import com.domain.auth.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService service;

    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserDto> findByUsername(@PathVariable(value = "username") String email) {
        return ResponseEntity.ok(service.findByEmail(email));
    }

    @GetMapping("/get-default-role/{username}")
    public ResponseEntity<String> getDefaultRole(@PathVariable(value = "username") String email) {
        return ResponseEntity.ok(service.findByEmail(email).defaultRole().getName());
    }
}
