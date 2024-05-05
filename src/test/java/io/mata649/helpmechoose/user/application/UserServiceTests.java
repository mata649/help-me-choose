package io.mata649.helpmechoose.user.application;

import io.mata649.helpmechoose.authentication.exceptions.ForbiddenException;
import io.mata649.helpmechoose.role.Role;
import io.mata649.helpmechoose.user.application.dto.ChangePasswordRequest;
import io.mata649.helpmechoose.user.application.dto.CreateUserRequest;
import io.mata649.helpmechoose.user.application.dto.UpdateUserRequest;
import io.mata649.helpmechoose.user.application.dto.UserResponse;
import io.mata649.helpmechoose.user.exceptions.UserNotFoundException;
import io.mata649.helpmechoose.user.exceptions.UsernameAlreadyTakenException;
import io.mata649.helpmechoose.user.model.User;
import io.mata649.helpmechoose.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void create_whenUsernameAlreadyExists_throwsUsernameAlreadyTakenException() {
        CreateUserRequest request = new CreateUserRequest("mata649", "enough_good_password");
        when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(new User()));
        UsernameAlreadyTakenException exception = assertThrows(UsernameAlreadyTakenException.class, () ->
                userService.create(request));
        assertEquals("The username has been already taken", exception.getMessage());
    }

    @Test
    public void create_whenRequestIsGood_returnsUserResponse() {

        CreateUserRequest request = new CreateUserRequest("mata649", "enough_good_password");
        User userSaved = User.builder()
                .id(UUID.randomUUID())
                .username(request.username())
                .role(Role.USER)
                .build();
        UserResponse expectedResponse = UserResponse.fromUser(userSaved);
        when(userRepository.findByUsername(request.username())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userSaved);
        when(passwordEncoder.encode(request.password())).thenReturn("123456");
        UserResponse resp = userService.create(request);

        assertEquals(expectedResponse, resp);
    }

    @Test
    public void update_whenUserIsNotFound_throwsUserNotFoundException() {
        UpdateUserRequest request = new UpdateUserRequest(UUID.randomUUID(), "mata649", UUID.randomUUID(), Role.USER);

        when(userRepository.findById(request.id())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.update(request));
        assertEquals("User was not found", exception.getMessage());
    }

    @Test
    public void update_whenMismatchedIDAndRole_throwsForbiddenException() {
        UpdateUserRequest request = new UpdateUserRequest(UUID.randomUUID(), "mata649", UUID.randomUUID(), Role.USER);
        User userFound = User.builder().id(UUID.randomUUID()).username("john_doe").role(Role.USER).build();

        when(userRepository.findById(request.id())).thenReturn(Optional.of(userFound));

        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> userService.update(request));
        assertEquals("You're not allowed to perform this action", exception.getMessage());
    }

    @Test
    public void update_whenIDMatchesCurrentUserButUsernameAlreadyExists_throwsUsernameNotFoundException() {
        UUID userID = UUID.randomUUID();
        UpdateUserRequest request = new UpdateUserRequest(userID, "mata649", userID, Role.USER);
        User userFoundByID = User.builder().id(request.id()).username("john_doe").role(Role.USER).build();
        User userFoundByUsername = User.builder().id(UUID.randomUUID()).username("mata649").role(Role.USER).build();

        when(userRepository.findById(request.id())).thenReturn(Optional.of(userFoundByID));
        when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(userFoundByUsername));

        UsernameAlreadyTakenException exception = assertThrows(UsernameAlreadyTakenException.class, () -> userService.update(request));

        assertEquals("The username has been already taken", exception.getMessage());
    }

    @Test
    public void update_whenRoleIsAdministratorButUsernameAlreadyExists_throwsUsernameNotFoundException() {

        UpdateUserRequest request = new UpdateUserRequest(UUID.randomUUID(), "mata649", UUID.randomUUID(), Role.ADMINISTRATOR);
        User userFoundByID = User.builder().id(UUID.randomUUID()).username("john_doe").role(Role.USER).build();
        User userFoundByUsername = User.builder().id(UUID.randomUUID()).username("mata649").role(Role.USER).build();

        when(userRepository.findById(request.id())).thenReturn(Optional.of(userFoundByID));
        when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(userFoundByUsername));

        UsernameAlreadyTakenException exception = assertThrows(UsernameAlreadyTakenException.class, () -> userService.update(request));

        assertEquals("The username has been already taken", exception.getMessage());
    }

    @Test
    public void update_whenUserIsUpdating_returnsUserResponse() {
        UUID userID = UUID.randomUUID();
        UpdateUserRequest request = new UpdateUserRequest(userID, "mata649", userID, Role.ADMINISTRATOR);
        User userFoundByID = User.builder().id(userID).username("mata648").role(Role.USER).build();
        UserResponse expectedResponse = new UserResponse(request.id(), request.username(), userFoundByID.getRole());
        when(userRepository.findById(request.id())).thenReturn(Optional.of(userFoundByID));
        when(userRepository.findByUsername(request.username())).thenReturn(Optional.empty());
        UserResponse resp = userService.update(request);
        assertEquals(expectedResponse, resp);
        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    public void update_whenAdministratorIsUpdating_returnsUserResponse() {
        UUID userID = UUID.randomUUID();
        UpdateUserRequest request = new UpdateUserRequest(userID, "mata649", UUID.randomUUID(), Role.ADMINISTRATOR);
        User userFoundByID = User.builder().id(userID).username("mata649").role(Role.USER).build();
        User userFoundByUsername = User.builder().id(userID).username("mata649").role(Role.USER).build();
        UserResponse expectedResponse = new UserResponse(userFoundByID.getId(), request.username(), userFoundByID.getRole());
        when(userRepository.findById(request.id())).thenReturn(Optional.of(userFoundByID));
        when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(userFoundByUsername));
        UserResponse resp = userService.update(request);
        assertEquals(expectedResponse, resp);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void changePassword_whenUserIsNotFound_throwsUserNotFoundException() {
        ChangePasswordRequest request = new ChangePasswordRequest(UUID.randomUUID(),
                "weird_password",
                "better_password",
                UUID.randomUUID(), Role.USER);
        when(userRepository.findById(request.id())).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.changePassword(request));
        assertEquals("User was not found", exception.getMessage());
    }

    @Test
    public void changePassword_whenMismatchedIDAndRole_throwsForbiddenException() {
        ChangePasswordRequest request = new ChangePasswordRequest(UUID.randomUUID(),
                "weird_password",
                "better_password",
                UUID.randomUUID(), Role.USER);

        User userFound = User.builder().id(UUID.randomUUID()).username("john_doe").role(Role.USER).build();

        when(userRepository.findById(request.id())).thenReturn(Optional.of(userFound));

        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> userService.changePassword(request));
        assertEquals("You're not allowed to perform this action", exception.getMessage());
    }

    @Test
    public void changePassword_whenIDMatchesButPasswordIsInvalid_throwsForbiddenException() {
        UUID userID = UUID.randomUUID();
        ChangePasswordRequest request = new ChangePasswordRequest(userID,
                "weird_password",
                "better_password",
                userID, Role.USER);

        User userFound = User.builder().id(request.id()).username("john_doe").role(Role.USER).password("this_is_not_the_weird_password").build();

        when(userRepository.findById(request.id())).thenReturn(Optional.of(userFound));
        when(passwordEncoder.matches(request.oldPassword(), userFound.getPassword())).thenReturn(false);
        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> userService.changePassword(request));
        assertEquals("You're not allowed to perform this action", exception.getMessage());
    }

    @Test
    public void changePassword_whenRoleMatchesButPasswordIsInvalid_returnsUserResponse() {
        ChangePasswordRequest request = new ChangePasswordRequest(UUID.randomUUID(),
                "weird_password",
                "better_password",
                UUID.randomUUID(), Role.ADMINISTRATOR);

        User userFound = User.builder().id(request.id()).username("john_doe").role(Role.USER).password("this_is_not_the_weird_password").build();
        String newPasswordHashed = "new password hashed";
        UserResponse expectedResponse = UserResponse.fromUser(userFound);
        when(userRepository.findById(request.id())).thenReturn(Optional.of(userFound));
        when(passwordEncoder.matches(request.oldPassword(), userFound.getPassword())).thenReturn(false);
        when(passwordEncoder.encode(request.newPassword())).thenReturn(newPasswordHashed);
        UserResponse resp = userService.changePassword(request);

        assertEquals(expectedResponse, resp);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void changePassword_whenIDMatchesAndPasswordsMatch_returnsUserResponse() {
        UUID userID = UUID.randomUUID();
        ChangePasswordRequest request = new ChangePasswordRequest(userID,
                "weird_password",
                "better_password",
                userID, Role.USER);

        User userFound = User.builder().id(request.id()).username("john_doe").role(Role.USER).password("weird_password").build();
        String newPasswordHashed = "new password hashed";
        UserResponse expectedResponse = UserResponse.fromUser(userFound);
        when(userRepository.findById(request.id())).thenReturn(Optional.of(userFound));
        when(passwordEncoder.matches(request.oldPassword(), userFound.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(request.newPassword())).thenReturn(newPasswordHashed);
        UserResponse resp = userService.changePassword(request);

        assertEquals(expectedResponse, resp);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void findByID_whenUserIsNotFound_throwsUserNotFoundException() {
        UUID userID = UUID.randomUUID();
        when(userRepository.findById(userID)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.findById(userID));

        assertEquals("User was not found", exception.getMessage());
    }

    @Test
    public void findByID_whenUserIsFound_returnsUserResponse() {
        UUID userID = UUID.randomUUID();
        User userFound = User.builder().id(userID).username("mata649").role(Role.USER).build();
        when(userRepository.findById(userID)).thenReturn(Optional.of(userFound));
        UserResponse expectedResponse = UserResponse.fromUser(userFound);
        UserResponse resp = userService.findById(userID);
        assertEquals(expectedResponse, resp);
    }

    @Test
    public void findByUsername_whenUserIsNotFound_throwsUserNotFoundException() {
        String username = "mata649";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.findByUsername(username));

        assertEquals("User was not found", exception.getMessage());
    }

    @Test
    public void findByUsername_whenUserIsFound_returnsUserResponse() {
        String username = "mata649";
        User userFound = User.builder().id(UUID.randomUUID()).username("mata649").role(Role.USER).build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userFound));
        UserResponse expectedResponse = UserResponse.fromUser(userFound);
        UserResponse resp = userService.findByUsername(username);
        assertEquals(expectedResponse, resp);
    }


}
