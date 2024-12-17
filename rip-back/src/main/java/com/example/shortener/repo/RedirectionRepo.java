package com.example.shortener.repo;

import com.example.shortener.model.Redirection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Repository
@RequiredArgsConstructor
public class RedirectionRepo {
    private final RestTemplate restTemplate;

    @Value("${application.db-service.host}/redirection")
    private String url;

    public Redirection save(Redirection redirection) {
        var response = restTemplate.postForEntity(url, redirection, Redirection.class);
        redirection.setId(
                Objects.requireNonNull(response.getBody()).getId()
        );
        return redirection;
    }

    public void delete(long id, long userId) {
        restTemplate.delete(String.format("%s?id={id}&userId={userId}", url), Map.of(
                "id", id,
                "userId", userId
        ));
    }

    public Optional<Redirection> findById(long id, long userId) {
        try {
            var response = restTemplate.getForEntity(String.format("%s?id={id}&userId={userId}", url),
                    Redirection.class, Map.of(
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
            var response = restTemplate.getForEntity(String.format("%s/find?shortKey={shortKey}", url), Redirection.class, Map.of(
                    "shortKey", shortKey
            ));
            return Optional.ofNullable(response.getBody());
        } catch (RestClientException e) {
            return Optional.empty();
        }

    }

    public List<Redirection> findAllByUserId(long userId) {
        try {
            final var response = restTemplate.getForEntity(
                    String.format("%s/all?userId={userId}", url),
                    Redirection[].class,
                    Map.of(
                            "userId", userId
                    )
            );
            return Arrays.stream(Objects.requireNonNull(response.getBody())).toList();
        } catch (RestClientException e) {
            return List.of();
        }
    }
}
