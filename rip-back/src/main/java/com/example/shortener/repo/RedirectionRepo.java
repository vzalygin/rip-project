package com.example.shortener.repo;

import com.example.shortener.model.Redirection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class RedirectionRepo {
    private final RestTemplate restTemplate;

    @Value("${db-service.host}/redirection")
    private String url;

    public void save(Redirection redirection) {
        var response = restTemplate.postForEntity(url, redirection, Redirection.class);
        throwOnFailure(response);
        redirection.setId(
                Objects.requireNonNull(response.getBody()).getId()
        );
    }

    public void delete(long id, long userId) {
        restTemplate.delete(url, Map.of(
                "id", id,
                "userId", userId
        ));
    }

    public Optional<Redirection> findById(long id, long userId) {
        var response = restTemplate.getForEntity(url, Redirection.class, Map.of(
                "id", id,
                "userId", userId
        ));
        throwOnFailure(response);
        return Optional.ofNullable(response.getBody());
    }

    public Optional<Redirection> findByShortKey(String shortKey) {
        var response = restTemplate.getForEntity(url, Redirection.class, Map.of(
                "shortKey", shortKey
        ));
        throwOnFailure(response);
        return Optional.ofNullable(response.getBody());
    }

    private void throwOnFailure(ResponseEntity<?> response) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new HttpClientErrorException(response.getStatusCode(), "failed to send request");
        }
    }
}
