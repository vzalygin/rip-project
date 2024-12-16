package com.example.shortener.services;

import com.example.shortener.model.Redirection;
import com.example.shortener.model.*;
import com.example.shortener.repo.RedirectionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {
    private final RedirectionRepo repo;
    private final RandomKeyGen gen;
    private final UrlValidator validator;

    @Value("${application.short-key-size}")
    private Integer shortKeySize;

    @Value("${application.short-url-prefix}}")
    private String shortUrlPrefix;

    public String shorten(String longUrl, long userId) {
        String validationError = validator.validateAndGetError(longUrl);
        if (validationError != null) {
            throw new InvalidUrlException(validationError);
        }
        String shortKey = gen.generateKey(shortKeySize);

        var redirection = new Redirection(longUrl, shortKey, userId);
        repo.save(redirection);

        return formatShortUrl(shortKey);
    }

    private String formatShortUrl(String key) {
        return String.format("%s/%s", shortUrlPrefix, key);
    }

    public Redirection resolve(String shortKey) throws RedirectionNotFoundException {
        Optional<Redirection> redirectionO = repo.findByShortKey(shortKey);
        if (redirectionO.isPresent()) {
            var redirection = redirectionO.get();
            redirection.incrementUsageCount();
            repo.save(redirection);
            return redirection;
        }
        throw new RedirectionNotFoundException(shortKey);
    }

    public void deleteRedirection(long id, long userId) {
        repo.delete(id, userId);
    }

    public Redirection getRedirection(long id, long userId) {
        return repo
                .findById(id, userId)
                .orElseThrow(() -> new RedirectionNotFoundException(Long.toString(id)));
    }
}
