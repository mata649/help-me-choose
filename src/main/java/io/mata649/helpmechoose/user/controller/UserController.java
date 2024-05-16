package io.mata649.helpmechoose.user.controller;

import io.mata649.helpmechoose.user.application.UserService;
import io.mata649.helpmechoose.user.application.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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
}
