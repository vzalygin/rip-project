package com.example.shortener.controllers;

import com.example.shortener.services.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/a")
@RequiredArgsConstructor
public class AController {
    private final UrlShortenerService shortener;

    @RequestMapping("/{shortKey}")
    @Operation(summary = "Выполнение перенаправления")
    public void doRedirect(@PathVariable String shortKey, HttpServletResponse response) {
        var redirection = shortener.resolve(shortKey);
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", redirection.getLongUrl());
    }
}
