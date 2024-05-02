package io.mata649.helpmechoose.authentication.application.dto;

import io.mata649.helpmechoose.role.Role;

import java.util.UUID;

public record AuthenticatedUserResponse(
        UUID id,
        String username,
        Role role,
        String jwt
) {
}
