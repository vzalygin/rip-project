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

    @Value("${shortKeySize:3}")
    private Integer shortKeySize;

    @Value("${application.domain:localhost}")
    private String appDomain;

    @Value("${application.protocol:http}")
    private String protocol;

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    UrlValidator validator;

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

    private String formatShortUrl(String tail) {
        return protocol + "://" + appDomain + ":" + serverPort + "/a/" + tail;
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
