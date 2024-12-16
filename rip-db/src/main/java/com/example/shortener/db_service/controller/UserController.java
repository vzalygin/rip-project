package com.example.shortener.db_service.controller;


import com.example.shortener.db_service.model.User;
import com.example.shortener.db_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/")
    public Optional<User> getUserById(@RequestParam("username") String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping("/")
    public User saveUser(@RequestBody User user) {
        return userService.save(user);
    }
}
