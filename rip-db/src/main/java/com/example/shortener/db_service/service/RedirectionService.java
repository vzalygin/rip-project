package com.example.shortener.db_service.service;

import com.example.shortener.db_service.dto.RedirectionDTO;
import com.example.shortener.db_service.dto.UserDTO;
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

    public Optional<RedirectionDTO> findRedirectionByShortKey(String shortKey) {
        return redirectionRepo.findByShortKey(shortKey).map(RedirectionDTO::fromEntity);
    }

    public Optional<RedirectionDTO> getRedirection(long id, long userId) {
        return redirectionRepo.findByIdAndUserId(id, userId).map(RedirectionDTO::fromEntity);
    }

    public RedirectionDTO save(RedirectionDTO redirection) {
        return RedirectionDTO.fromEntity(redirectionRepo.save(redirection.toEntity()));
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
