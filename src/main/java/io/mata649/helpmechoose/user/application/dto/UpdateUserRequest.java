package io.mata649.helpmechoose.user.application.dto;

import io.mata649.helpmechoose.role.Role;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UpdateUserRequest(
        UUID id,
        @Size(min = 1, max = 40)
        String username,
        UUID currentUser,
        Role currentUserRole) {
}
