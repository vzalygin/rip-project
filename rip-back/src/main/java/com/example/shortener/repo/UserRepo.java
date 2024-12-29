package com.example.shortener.repo;

import com.example.shortener.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepo {
    private static final Logger log = LoggerFactory.getLogger(UserRepo.class);
    private final RestTemplate restTemplate;

    @Value("${application.db-service.host}/user")
    private String url;

    public User save(User user) {
        var response = restTemplate.postForEntity(url, user, User.class);
        user.setId(Objects.requireNonNull(response.getBody()).getId());
        return user;
    }

    public Optional<User> findByUsername(String username) {
        try {
            var response = restTemplate.getForEntity(
                    String.format("%s?username={username}", url),
                    User.class, Map.of("username", username)
            );
            return Optional.ofNullable(response.getBody());
        } catch (RestClientException e) {
            return Optional.empty();
        }
    }
}
