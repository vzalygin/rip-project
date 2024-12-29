package com.example.shortener.model;

public class UserAlreadyExistsException extends ModelException {
    public UserAlreadyExistsException(String username) {
        super(String.format("User with username = %s already exists", username));
    }
}
