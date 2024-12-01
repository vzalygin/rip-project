package com.example.shortener.security;

import java.util.List;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;

@Getter
public class UserDetails1 extends User {
    private final long userId;

    public UserDetails1(String username, String password, long userId) {
        super(username, password, List.of());
        this.userId = userId;
    }
}
