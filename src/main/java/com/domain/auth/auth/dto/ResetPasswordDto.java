package com.domain.auth.auth.dto;

public record ResetPasswordDto(String email, String currentPassword, String newPassword) {
}
