package com.example.shortener.repo;

import com.example.shortener.model.Redirection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class RedirectionRepo {
    private final RestTemplate restTemplate;

    @Value("${application.db-service.host}/redirection")
    private String url;

    public void save(Redirection redirection) {
        var response = restTemplate.postForEntity(url, redirection, Redirection.class);
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
        try {
            var response = restTemplate.getForEntity(url, Redirection.class, Map.of(
                    "id", id,
                    "userId", userId
            ));
            return Optional.ofNullable(response.getBody());
        } catch (RestClientException e) {
            return Optional.empty();
        }
    }

    public Optional<Redirection> findByShortKey(String shortKey) {
        try {
            var response = restTemplate.getForEntity(url+"/find", Redirection.class, Map.of(
                    "shortKey", shortKey
            ));
            return Optional.ofNullable(response.getBody());
        } catch (RestClientException e) {
            return Optional.empty();
        }

    }
}
