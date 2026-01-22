package com.domain.auth.auth.controller;

import com.domain.auth.auth.dto.AuthDto;
import com.domain.auth.auth.dto.LoginDto;
import com.domain.auth.auth.dto.RegisterDto;
import com.domain.auth.auth.service.AuthService;
import com.domain.auth.user.dto.UserDto;
import com.domain.auth.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthDto> login(@RequestBody LoginDto dto, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(dto, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(value = "refresh_token", required = false) UUID refreshToken, HttpServletResponse response) {
        authService.logout(refreshToken, response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthDto> refresh(
            @CookieValue(value = "refresh_token") UUID refreshToken,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.refresh(refreshToken, response));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDto dto) {
        authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/register-with-role")
    public ResponseEntity<Void> registerWithRole(@RequestBody RegisterDto dto, @RequestParam(value = "role") String roleName) {
        authService.registerWithDefaultRole(dto, roleName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/verify-user/{username}")
    public ResponseEntity<String> verifyUser(@PathVariable(value = "username") String email) {
        return ResponseEntity.ok(userService.findByEmail(email).getDefaultRole().getName());
    }

    @PostMapping("/validate")
    public ResponseEntity<UserDto> validate(@RequestBody AuthDto dto) {
        return ResponseEntity.ok(authService.validate(dto));
    }
}
