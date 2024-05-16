package io.mata649.helpmechoose.user.exceptions;

import io.mata649.helpmechoose.shared.exceptions.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> userNotFoundExceptionHandler(UserNotFoundException exc, ServletWebRequest request) {
        return new ResponseEntity<>(
                ApiError.buildBasicError(exc, request, HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<ApiError> usernameAlreadyTaken(UsernameAlreadyTakenException exc, ServletWebRequest request) {
        return new ResponseEntity<>(
                ApiError.buildBasicError(exc, request, HttpStatus.CONFLICT),
                HttpStatus.CONFLICT);
    }

}
