package com.fredrik.matladan.user.exceptions;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException (String message) {
        super("User with email "  + message + " already exists in the database" );
    }
}
