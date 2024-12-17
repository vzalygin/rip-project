package com.example.shortener.db_service.controller;

import com.example.shortener.db_service.dto.RedirectionDTO;
import com.example.shortener.db_service.model.Redirection;
import com.example.shortener.db_service.service.RedirectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/redirection")
@RequiredArgsConstructor
public class RedirectionController {
    private final RedirectionService redirectionService;

    @GetMapping("/find")
    public Optional<RedirectionDTO> findRedirectionByKey(
            @RequestParam("shortKey") String shortKey
    ) {
        return redirectionService.findRedirectionByShortKey(shortKey);
    }

    @GetMapping("/all")
    public List<RedirectionDTO> findAllByUserId(
            @RequestParam("userId") long userId
    ) {
        return redirectionService.getRedirectionsByUserId(userId);
    }

    @GetMapping
    public Optional<RedirectionDTO> getRedirectionById(
            @RequestParam("id") long id,
            @RequestParam("userId") long userId
    ) {
        return redirectionService.getRedirection(id, userId);
    }

    @PostMapping
    public RedirectionDTO saveRedirection(@RequestBody RedirectionDTO redirection) {
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
