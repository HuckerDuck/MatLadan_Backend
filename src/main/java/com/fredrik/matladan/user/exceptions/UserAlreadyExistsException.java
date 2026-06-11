package com.fredrik.matladan.user.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException (String message) {
        super("User with email "  + message + " already exists in the database" );
    }
}
