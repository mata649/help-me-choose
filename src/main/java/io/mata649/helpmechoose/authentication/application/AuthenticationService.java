package io.mata649.helpmechoose.authentication.application;

import io.mata649.helpmechoose.authentication.application.dto.AuthenticatedUserResponse;
import io.mata649.helpmechoose.authentication.application.dto.LoginUserRequest;
import io.mata649.helpmechoose.authentication.application.dto.RegisterUserRequest;
import io.mata649.helpmechoose.authentication.exceptions.PasswordsDontMatchException;
import io.mata649.helpmechoose.user.application.UserService;
import io.mata649.helpmechoose.user.application.dto.CreateUserRequest;
import io.mata649.helpmechoose.user.application.dto.UserResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticatedUserResponse register(RegisterUserRequest request) {
        if (!request.password().equals(request.repeatedPassword()))
            throw new PasswordsDontMatchException();
        CreateUserRequest createUserRequest = new CreateUserRequest(request.username(), request.password());
        UserResponse resp = userService.create(createUserRequest);
        String jwt = jwtService.generateToken(resp.username(), generateExtraClaims(resp));
        return new AuthenticatedUserResponse(resp.id(), resp.username(), resp.role(), jwt);
    }

    private Map<String, Object> generateExtraClaims(UserResponse resp) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", resp.role());
        extraClaims.put("currentUserID", resp.id());
        return extraClaims;
    }

    public AuthenticatedUserResponse login(LoginUserRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        authenticationManager.authenticate(authToken);
        UserResponse resp = userService.findByUsername(request.username());
        String jwt = jwtService.generateToken(resp.username(), generateExtraClaims(resp));
        return new AuthenticatedUserResponse(resp.id(), resp.username(), resp.role(), jwt);
    }

}
