package com.ryanstuckey0.stat_saga_backend.exception;

public class UsernameTakenException extends Exception {
    public UsernameTakenException(String username) {
        super("Username " + username + " is already taken.");
    }
}
