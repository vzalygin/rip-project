package com.example.shortener.db_service.controller;


import com.example.shortener.db_service.dto.UserDTO;
import com.example.shortener.db_service.model.User;
import com.example.shortener.db_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user")
    public Optional<UserDTO> getUserById(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping("/user")
    public User saveUser(@RequestBody UserDTO user) {
        return userService.save(user);
    }
}
