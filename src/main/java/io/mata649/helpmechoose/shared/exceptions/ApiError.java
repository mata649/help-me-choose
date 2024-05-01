package io.mata649.helpmechoose.shared.exceptions;

public record ApiError(String message, Integer statusCode, String path, String method) {
}
