package com.example.shortener.services;

import com.example.shortener.model.Redirection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@Repository
@AllArgsConstructor
public class RedirectionRepo {
    private final RestTemplate restTemplate;

    void save(Redirection redirection) {

    }

    void update(Redirection redirection) {

    }

    void delete(long id, long userId) {

    }

    Optional<Redirection> findById(long id, long userId) {

    }

    Optional<Redirection> findByShortKey(String shortKey) {

    }
}
