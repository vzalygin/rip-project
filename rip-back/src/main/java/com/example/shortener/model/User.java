package com.example.shortener.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class User {
    private Long id;
    private String username;
    private String passwordHash;

    public User(String username, String passwordHash) {
        this.passwordHash = passwordHash;
        this.username = username;
        this.id = null;
    }

    public User(long id, String username, String passwordHash) {
        this.passwordHash = passwordHash;
        this.username = username;
        this.id = id;
    }
}
