package io.mata649.helpmechoose.user.application.dto;

import java.util.UUID;

import io.mata649.helpmechoose.role.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ChangePasswordRequest {
    @NotNull
    public UUID id;

    @NotNull
    public String oldPassword;

    @NotNull
    @Size(min = 8, max = 64)
    public String newPassword;

    @NotNull
    public UUID currentUserID;

    @NotNull
    public Role currentUserRole;

}
