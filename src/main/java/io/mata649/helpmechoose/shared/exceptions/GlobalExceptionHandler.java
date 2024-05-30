package io.mata649.helpmechoose.shared.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exc,
            ServletWebRequest request) {
        ArrayList<FieldError> errors = new ArrayList<>();

        if (exc.getDetailMessageArguments() != null) {
            errors = Arrays.stream(exc.getDetailMessageArguments())
                    .filter(err -> ((String) err).split(":").length == 2)
                    .map(err -> {
                        String error = (String) err;
                        String[] errorSplited = error.replace(" ", "").split(":");
                        return new FieldError(errorSplited[0], errorSplited[1]);
                    }).collect(Collectors.toCollection(ArrayList::new));
        }
        return new ResponseEntity<>(
                ApiError.builder().message(errors)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .path(request.getRequest().getRequestURL().toString())
                        .method(request.getRequest().getMethod())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> httpMEssageNotReadableExceptionHandler(HttpMessageNotReadableException exc,
            ServletWebRequest request) {

        return new ResponseEntity<>(
                ApiError.builder().message("the request body is requred")
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .path(request.getRequest().getRequestURL().toString())
                        .method(request.getRequest().getMethod())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

}
