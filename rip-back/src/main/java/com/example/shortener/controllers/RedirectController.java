package com.example.shortener.controllers;

import com.example.shortener.messages.CreateRedirectRequest;
import com.example.shortener.messages.CreateRedirectResponse;
import com.example.shortener.messages.GetStatsResponse;
import com.example.shortener.security.UserDetails1;
import com.example.shortener.services.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/redirection")
@AllArgsConstructor
public class RedirectController {
    private final UrlShortenerService shortener;

    @PostMapping("")
    @Operation(summary = "Создание перенаправления", security = { @SecurityRequirement(name = "bearer-key") })
    public CreateRedirectResponse createRedirect(
            @RequestBody CreateRedirectRequest request,
            @AuthenticationPrincipal UserDetails1 userDetails
    ) {
        return shortener.shorten(request.longUrl(), userDetails.getUserId());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение информации по перенаправлению", security = { @SecurityRequirement(name = "bearer-key") })
    public GetStatsResponse getStats(
            @PathVariable long id,
            @AuthenticationPrincipal UserDetails1 userDetails
    ) {
        return shortener.getRedirection(id, userDetails.getUserId());
    }

    @GetMapping("/all")
    @Operation(summary = "Получение информации по перенаправлениям пользователя", security = { @SecurityRequirement(name = "bearer-key") })
    public List<GetStatsResponse> getStatsAll(
            @AuthenticationPrincipal UserDetails1 userDetails
    ) {
        return shortener.getAllUserRedirections(userDetails.getUserId());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление перенаправления", security = { @SecurityRequirement(name = "bearer-key") })
    public void deleteRedirection(
            @PathVariable long id,
            @AuthenticationPrincipal UserDetails1 userDetails
    ) {
        shortener.deleteRedirection(id, userDetails.getUserId());
    }
}
