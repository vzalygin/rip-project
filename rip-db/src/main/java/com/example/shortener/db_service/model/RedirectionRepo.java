package com.example.shortener.db_service.model;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface RedirectionRepo extends CrudRepository<Redirection, String> {
    Optional<Redirection> findBySecretKey(String secretKey);

    Optional<Redirection> findByShortKey(String shortKey);
}
