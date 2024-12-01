package com.example.shortener.repo;

import com.example.shortener.model.User;
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
public class UserRepo {
    private final RestTemplate restTemplate;

    @Value("${db-service.host}/user")
    private String url;

    public void save(User user) {
        var response = restTemplate.postForEntity(url, user, User.class);
        throwOnFailure(response);
        user.setId(Objects.requireNonNull(response.getBody()).getId());
    }

    public Optional<User> findById(long id) {
        var response = restTemplate.getForEntity(url, User.class, Map.of("id", id));
        throwOnFailure(response);
        return Optional.ofNullable(response.getBody());
    }

    public Optional<User> findByUsername(String username) {
        var response = restTemplate.getForEntity(url, User.class, Map.of("username", username));
        throwOnFailure(response);
        return Optional.ofNullable(response.getBody());
    }

    private void throwOnFailure(ResponseEntity<?> response) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new HttpClientErrorException(response.getStatusCode(), "failed to send request");
        }
    }
}
