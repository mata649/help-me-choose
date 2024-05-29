package io.mata649.helpmechoose.authentication.controller;

import io.mata649.helpmechoose.authentication.application.AuthenticationService;
import io.mata649.helpmechoose.authentication.application.dto.LoginUserRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.mata649.helpmechoose.authentication.application.dto.AuthenticatedUserResponse;
import io.mata649.helpmechoose.authentication.application.dto.RegisterUserRequest;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticatedUserResponse> register(@RequestBody @Valid RegisterUserRequest request) {
        AuthenticatedUserResponse response = authenticationService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticatedUserResponse> login(@RequestBody @Valid LoginUserRequest request) {
        AuthenticatedUserResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

}
