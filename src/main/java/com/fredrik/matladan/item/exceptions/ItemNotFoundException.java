package com.fredrik.matladan.item.exceptions;

public class ItemNotFoundException extends RuntimeException{
    public ItemNotFoundException(Long id) {
        super("Item with id " + id + " not found");
    }
}
