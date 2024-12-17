package com.example.shortener.services;

import com.example.shortener.messages.CreateRedirectResponse;
import com.example.shortener.messages.GetStatsResponse;
import com.example.shortener.model.InvalidUrlException;
import com.example.shortener.model.Redirection;
import com.example.shortener.model.RedirectionNotFoundException;
import com.example.shortener.repo.RedirectionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {
    private final RedirectionRepo repo;
    private final RandomKeyGen gen;
    private final UrlValidator validator;

    @Value("${application.short-key-size}")
    private Integer shortKeySize;

    @Value("${application.short-url-prefix}")
    private String shortUrlPrefix;

    public CreateRedirectResponse shorten(String longUrl, long userId) {
        String validationError = validator.validateAndGetError(longUrl);
        if (validationError != null) {
            throw new InvalidUrlException(validationError);
        }
        String shortKey = gen.generateKey(shortKeySize);

        var redirection = new Redirection(longUrl, shortKey, userId);
        repo.save(redirection);

        return new CreateRedirectResponse(formatShortUrl(shortKey), redirection.getId());
    }

    private String formatShortUrl(String key) {
        return String.format("%s/%s", shortUrlPrefix, key);
    }

    public Redirection resolve(String shortKey) throws RedirectionNotFoundException {
        Optional<Redirection> redirectionO = repo.findByShortKey(shortKey);

        if (redirectionO.isEmpty()) {
            throw new RedirectionNotFoundException(shortKey);
        }

        var redirection = redirectionO.get();
        redirection.incrementUsageCount();
        return repo.save(redirection);
    }

    public void deleteRedirection(long id, long userId) {
        repo.delete(id, userId);
    }

    public GetStatsResponse getRedirection(long id, long userId) {
        return repo
                .findById(id, userId)
                .map(redirection -> new GetStatsResponse(
                        redirection.getId(),
                        redirection.getShortKey(),
                        redirection.getCreationDate().toInstant().toString(),
                        redirection.getUsageCount()
                ))
                .orElseThrow(() -> new RedirectionNotFoundException(Long.toString(id)));
    }

    public List<GetStatsResponse> getAllUserRedirections(long userId) {
        return repo.findAllByUserId(userId).stream().map(redirection -> new GetStatsResponse(
                redirection.getId(),
                redirection.getShortKey(),
                redirection.getCreationDate().toInstant().toString(),
                redirection.getUsageCount()
        )).toList();
    }
}
