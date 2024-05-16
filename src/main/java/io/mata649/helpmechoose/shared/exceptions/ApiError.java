package io.mata649.helpmechoose.shared.exceptions;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;

@Builder
public record ApiError(Object message, Integer statusCode, String path, String method) {
    public static ApiError buildBasicError(RuntimeException exc, ServletWebRequest request, HttpStatus status) {
        return ApiError.builder()
                .message(exc.getMessage())
                .statusCode(status.value())
                .path(request.getRequest().getRequestURL().toString())
                .method(request.getRequest().getMethod())
                .build();
    }
}
