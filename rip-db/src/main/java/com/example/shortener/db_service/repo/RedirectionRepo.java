package com.example.shortener.db_service.repo;

import java.util.Optional;

import com.example.shortener.db_service.model.Redirection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface RedirectionRepo extends CrudRepository<Redirection, Long> {

    Optional<Redirection> findByShortKey(String shortKey);

    Optional<Redirection> findByIdAndUserId(long id, long userId);
}
