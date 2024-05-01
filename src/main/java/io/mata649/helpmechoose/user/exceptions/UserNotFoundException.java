package io.mata649.helpmechoose.user.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User was not found");
    }


}
