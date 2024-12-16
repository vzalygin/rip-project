package com.example.shortener.services;

import com.example.shortener.model.ModelException;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {
    void validateAndGetError(String password) {
        if (password.length() < 6) {
            throw new ModelException("Invalid password");
        }
    }
}
