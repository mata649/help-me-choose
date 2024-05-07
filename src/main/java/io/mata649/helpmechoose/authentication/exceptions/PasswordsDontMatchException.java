package io.mata649.helpmechoose.authentication.exceptions;

public class PasswordsDontMatchException extends RuntimeException {
    public PasswordsDontMatchException() {
        super("The provided passwords don't match");
    }
}
