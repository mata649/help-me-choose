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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse create(CreateUserRequest request) {
        Optional<User> userFound = userRepository.findByUsername(request.username());
        if (userFound.isPresent())
            throw new UsernameAlreadyTakenException();
        User user = User.builder()
                .username(request.username())
                .role(Role.USER)
                .build();
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
        return UserResponse.fromUser(user);
    }

    public UserResponse update(UpdateUserRequest request) {
        User userFound = userRepository.findById(request.id()).orElseThrow(UserNotFoundException::new);

        if (!userFound.getId().equals(request.currentUser()) || !request.currentUserRole().equals(Role.ADMINISTRATOR))
            throw new ForbiddenException();

        if (userRepository.findByUsername(request.username()).isPresent())
            throw new UsernameAlreadyTakenException();
        userFound.setUsername(request.username());

        userRepository.save(userFound);
        return UserResponse.fromUser(userFound);
    }

    public UserResponse changePassword(ChangePasswordRequest request) {
        User userFound = userRepository.findById(request.id()).orElseThrow(UserNotFoundException::new);
        if (!userFound.getId().equals(request.currentUser()) || !request.currentUserRole().equals(Role.ADMINISTRATOR))
            throw new ForbiddenException();

        if (!passwordEncoder.matches(request.oldPassword(), userFound.getPassword()))
            throw new ForbiddenException();
        userFound.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(userFound);
        return UserResponse.fromUser(userFound);
    }

    public UserResponse findById(UUID id) {
        User userFound = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return UserResponse.fromUser(userFound);
    }

    public Page<UserResponse> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserResponse::fromUser);
    }
}
