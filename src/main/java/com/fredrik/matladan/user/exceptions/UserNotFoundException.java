package com.fredrik.matladan.user.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String id) {
        super("User with id " + id + " not found");
    }
}
