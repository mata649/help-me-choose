package io.mata649.helpmechoose.authentication.application;

import io.mata649.helpmechoose.authentication.application.dto.AuthenticatedUserResponse;
import io.mata649.helpmechoose.authentication.application.dto.RegisterUserRequest;
import io.mata649.helpmechoose.authentication.exceptions.PasswordsDontMatchException;
import io.mata649.helpmechoose.role.Role;
import io.mata649.helpmechoose.user.application.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import io.mata649.helpmechoose.user.application.dto.CreateUserRequest;
import io.mata649.helpmechoose.user.application.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {
    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void register_whenPasswordsDontMatch_throwsPasswordDontMatchException() {
        RegisterUserRequest request = new RegisterUserRequest("mata649", "good_password", "different_password");

        PasswordsDontMatchException exception = assertThrows(PasswordsDontMatchException.class, () -> authenticationService.register(request));

        assertEquals("The provided passwords don't match", exception.getMessage());
    }

    @Test
    public void register_whenRequestIsFine_returnsAuthenticatedUserResponse() {
        RegisterUserRequest request = new RegisterUserRequest("mata649", "good_password", "good_password");
        UserResponse createdUserResponse = new UserResponse(UUID.randomUUID(), "mata649", Role.USER);
        String generatedToken = "this is a jwt token";
        AuthenticatedUserResponse expectedResponse = new AuthenticatedUserResponse(
                createdUserResponse.id(),
                createdUserResponse.username(),
                createdUserResponse.role(),
                generatedToken);
        when(userService.create(any(CreateUserRequest.class)))
                .thenReturn(createdUserResponse);
        when(jwtService.generateToken(eq(request.username()), anyMap())).thenReturn(generatedToken);
        AuthenticatedUserResponse resp = authenticationService.register(request);

        assertEquals(expectedResponse, resp);
    }

    /* TODO: Real question, I am not sure if I should test the login.
    *   There is not business logic there, there are just call to other external implementations*/
}
