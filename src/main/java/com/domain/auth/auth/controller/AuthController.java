package com.domain.auth.auth.controller;

import com.domain.auth.auth.dto.AuthDto;
import com.domain.auth.auth.dto.LoginDto;
import com.domain.auth.auth.dto.RegisterDto;
import com.domain.auth.auth.service.AuthService;
import com.domain.auth.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthDto> login(@RequestBody LoginDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDto dto) {
        authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/verify-user/{username}")
    public ResponseEntity<String> verifyUser(@PathVariable(value = "username") String email) {
        return ResponseEntity.ok(userService.findByEmail(email).getDefaultRole().getName());
    }
}
