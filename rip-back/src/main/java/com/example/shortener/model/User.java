package com.example.shortener.model;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String passwordHash;

    public User(String username, String passwordHash) {
        this.passwordHash = passwordHash;
        this.username = username;
        this.id = null;
    }
}
