package com.fredrik.matladan.item.exceptions;

public class UserIsNotLoggedInException extends RuntimeException{
    public UserIsNotLoggedInException() {
        super("User is not logged in");
    }

}
