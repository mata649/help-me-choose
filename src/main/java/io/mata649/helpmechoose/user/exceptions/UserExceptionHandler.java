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
        return new ResponseEntity<>(new ApiError(exc.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequest().getRequestURL().toString(),
                request.getRequest().getMethod()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameAlreadyTaken.class)
    public ResponseEntity<ApiError> usernameAlreadyTaken(UsernameAlreadyTaken exc, ServletWebRequest request) {
        return new ResponseEntity<>(new ApiError(exc.getMessage(),
                HttpStatus.CONFLICT.value(),
                request.getRequest().getRequestURL().toString(),
                request.getRequest().getMethod()), HttpStatus.CONFLICT);
    }

}
