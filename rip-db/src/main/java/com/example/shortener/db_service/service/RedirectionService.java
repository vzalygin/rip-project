package com.example.shortener.db_service.service;

import com.example.shortener.db_service.model.Redirection;
import com.example.shortener.db_service.model.User;
import com.example.shortener.db_service.repo.RedirectionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedirectionService {
    private final RedirectionRepo redirectionRepo;

    public Optional<Redirection> findRedirectionByShortKey(String shortKey) {
        return redirectionRepo.findByShortKey(shortKey);
    }

    public Optional<Redirection> getRedirection(long id, long userId) {
        return redirectionRepo.findByIdAndUserId(id, userId);
    }

    public Redirection save(Redirection redirection) {
        return redirectionRepo.save(redirection);
    }

    public void delete(long id, long userId) {
        var user = new User();
        user.setId(userId);
        var redirectionToDelete = new Redirection();
        redirectionToDelete.setId(id);
        redirectionToDelete.setUser(user);
        redirectionRepo.delete(redirectionToDelete);
    }
}
