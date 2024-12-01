package com.example.shortener.messages;

public record AuthenticateUserRequest(String username, String password) {
}
