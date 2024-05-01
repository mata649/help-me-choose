package io.mata649.helpmechoose.authentication.exceptions;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException() {
        super("You're not allowed to perform this action");
    }
}
