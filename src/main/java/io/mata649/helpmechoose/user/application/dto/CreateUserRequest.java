package io.mata649.helpmechoose.user.application.dto;

import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @Size(min = 1, max = 40)
        String username,
        @Size(min = 8, max = 64)
        String password
) {
}
