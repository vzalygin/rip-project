package com.example.shortener.model;

public class UserNotFoundException extends ModelException {
    public UserNotFoundException(String request) {
        super(request);
    }
}
