package io.mata649.helpmechoose.user.application.dto;

import io.mata649.helpmechoose.role.Role;
import io.mata649.helpmechoose.user.model.User;

import java.util.UUID;

public record UserResponse(UUID id,
                           String username,
                           Role role) {
    public static UserResponse fromUser(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }
}
