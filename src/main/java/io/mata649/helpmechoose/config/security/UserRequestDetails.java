package io.mata649.helpmechoose.config.security;

import io.mata649.helpmechoose.role.Role;
import io.mata649.helpmechoose.user.model.User;

import java.util.UUID;

public record UserRequestDetails(UUID currentID, Role currentRole) {
    public static UserRequestDetails fromUser(User user) {
        return new UserRequestDetails(user.getId(), user.getRole());
    }
}
