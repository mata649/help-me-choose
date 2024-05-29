package io.mata649.helpmechoose.user.application.dto;

import java.util.UUID;

import io.mata649.helpmechoose.role.Role;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserRequest {
    UUID id;
    @Size(min = 1, max = 40)
    String username;
    UUID currentUserID;
    Role currentUserRole;
}
