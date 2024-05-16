package io.mata649.helpmechoose.authentication.exceptions;

import io.mata649.helpmechoose.shared.exceptions.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

@ControllerAdvice
public class AuthenticationExceptionHandler {


    @ExceptionHandler(PasswordsDontMatchException.class)
    public ResponseEntity<ApiError> passwordDontMatchExceptionHandler(PasswordsDontMatchException exc, ServletWebRequest request) {
        return new ResponseEntity<>(
                ApiError.buildBasicError(exc, request, HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> invalidCredentialsExceptionHandler(InvalidCredentialsException exc, ServletWebRequest request) {
        return new ResponseEntity<>(
                ApiError.buildBasicError(exc, request, HttpStatus.UNAUTHORIZED),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> forbiddenExceptionHandler(ForbiddenException exc, ServletWebRequest request) {
        return new ResponseEntity<>(
                ApiError.buildBasicError(exc, request, HttpStatus.FORBIDDEN),
                HttpStatus.FORBIDDEN
        );
    }

}

