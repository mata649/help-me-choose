package io.mata649.helpmechoose.user.application.dto;

import io.mata649.helpmechoose.role.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ChangePasswordRequest(
        UUID id,
        @NotBlank
        String oldPassword,
        @Size(min = 8, max = 64)
        String newPassword,
        UUID currentUser,
        Role currentUserRole
) {
}
