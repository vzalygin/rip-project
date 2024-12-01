package com.example.shortener.services;

import com.example.shortener.model.Redirection;
import com.example.shortener.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UrlShortenerService {

    @Autowired
    private RedirectionRepo repo;
    @Autowired
    private RandomKeyGen gen;

    @Value("${shortKeySize}")
    private Integer shortKeySize = 3;

    @Value("${application.domain}")
    private String appDomain = "localhost";

    @Value("${application.protocol}")
    private String protocol = "http";

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
        return protocol + "://" + appDomain + ":" + serverPort + "/" + tail;
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
}
