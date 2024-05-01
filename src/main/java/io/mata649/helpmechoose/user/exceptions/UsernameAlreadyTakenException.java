package io.mata649.helpmechoose.user.exceptions;

public class UsernameAlreadyTakenException extends RuntimeException {
    public UsernameAlreadyTakenException() {
        super("The username has been already taken");
    }


}
