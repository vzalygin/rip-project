package com.example.shortener.services;

import com.example.shortener.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepo {
    void save(User user) {

    }

    Optional<User> findById(long id) {

    }

    Optional<User> findByUsernameAndHash(User user) {

    }
}
