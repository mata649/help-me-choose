package io.mata649.helpmechoose.authentication.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginUserRequest(
        @NotNull @Size(min = 1, max = 40)
        String username,
        @NotNull @Size(min = 1, max = 64)
        String password
) {
}
