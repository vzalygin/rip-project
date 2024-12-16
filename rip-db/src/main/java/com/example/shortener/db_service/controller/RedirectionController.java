package com.example.shortener.db_service.controller;

import com.example.shortener.db_service.model.Redirection;
import com.example.shortener.db_service.service.RedirectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/redirection")
@RequiredArgsConstructor
public class RedirectionController {
    private final RedirectionService redirectionService;

    @GetMapping("/find")
    public Optional<Redirection> findRedirectionByKey(
            @RequestParam("shortKey") String shortKey
    ) {
        return redirectionService.findRedirectionByShortKey(shortKey);
    }

    @GetMapping
    public Optional<Redirection> getRedirectionById(
            @RequestParam("id") long id,
            @RequestParam("userId") long userId
    ) {
        return redirectionService.getRedirection(id, userId);
    }

    @PostMapping
    public Redirection saveRedirection(@RequestBody Redirection redirection) {
        return redirectionService.save(redirection);
    }

    @DeleteMapping
    public void deleteRedirection(
            @RequestParam("id") long id,
            @RequestParam("userId") long userId
    ) {
        redirectionService.delete(id, userId);
    }
}
