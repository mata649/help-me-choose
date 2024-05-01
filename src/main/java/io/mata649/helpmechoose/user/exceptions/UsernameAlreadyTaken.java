package io.mata649.helpmechoose.user.exceptions;

public class UsernameAlreadyTaken extends RuntimeException {
    public UsernameAlreadyTaken() {
        super("The username has been already taken");
    }


}
