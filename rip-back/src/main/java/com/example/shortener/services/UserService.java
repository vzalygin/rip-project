package com.example.shortener.services;

import com.example.shortener.model.User;
import com.example.shortener.model.UserNotFoundException;
import com.example.shortener.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final MessageDigest digest = MessageDigest.getInstance("SHA-256");

    public UserService(UserRepo userRepo) throws NoSuchAlgorithmException {
        this.userRepo = userRepo;
    }

    public void register(String username, String password) {
        var hash = makePasswordHash(password);
        var user = new User(username, hash);
        userRepo.save(user);
    }

//    public String login(String username, String password) {
//        var user0 = userRepo.findByUsername(username);
//        var user = user0.orElseThrow(() -> new UserNotFoundException(username));
//
//    }

    private String makePasswordHash(String password) {
        var bytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
