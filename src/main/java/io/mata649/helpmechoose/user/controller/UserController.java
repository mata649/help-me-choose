package io.mata649.helpmechoose.user.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.mata649.helpmechoose.config.security.UserRequestDetails;
import io.mata649.helpmechoose.user.application.UserService;
import io.mata649.helpmechoose.user.application.dto.ChangePasswordRequest;
import io.mata649.helpmechoose.user.application.dto.UpdateUserRequest;
import io.mata649.helpmechoose.user.application.dto.UserResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@EnableWebSecurity
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMINISTRATOR')")
    @GetMapping("/")
    public Page<UserResponse> find(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMINISTRATOR')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findByID(@PathVariable("id") UUID id) {
        UserResponse response = userService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMINISTRATOR')")
    @PutMapping("/{id}/changePassword")
    public ResponseEntity<UserResponse> changePassword(@PathVariable("id") UUID id,
            @RequestBody @Valid ChangePasswordRequest request) {
        UserRequestDetails currentUser = (UserRequestDetails) SecurityContextHolder.getContext().getAuthentication()
                .getDetails();
        request.setCurrentUserID(currentUser.currentID());
        request.setCurrentUserRole(currentUser.currentRole());
        UserResponse response = userService.changePassword(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMINISTRATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable("id") UUID id,
            @RequestBody @Valid UpdateUserRequest request) {
        UserRequestDetails currentUser = (UserRequestDetails) SecurityContextHolder.getContext().getAuthentication()
                .getDetails();
        request.setCurrentUserRole(currentUser.currentRole());
        request.setCurrentUserID(currentUser.currentID());
        UserResponse response = userService.update(request);
        return ResponseEntity.ok(response);
    }
}
