package com.example.shortener.security;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

@Getter
public class UserDetails extends User {
    private final long userId;

    public UserDetails(String username, String password, long userId) {
        super(username, password, List.of());
        this.userId = userId;
    }
}
