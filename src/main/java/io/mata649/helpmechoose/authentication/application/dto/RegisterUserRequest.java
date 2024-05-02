package io.mata649.helpmechoose.authentication.application.dto;

import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @Size(min = 1, max = 40)
        String username,
        @Size(min = 8, max = 64)
        String password,
        @Size(min = 1, max = 64)
        String repeatedPassword
) {
}
