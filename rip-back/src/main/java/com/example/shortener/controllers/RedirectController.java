package com.example.shortener.controllers;

import com.example.shortener.messages.CreateRedirectRequest;
import com.example.shortener.messages.CreateRedirectResponse;
import com.example.shortener.messages.GetStatsResponse;
import com.example.shortener.security.UserDetails1;
import com.example.shortener.services.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redirection")
@AllArgsConstructor
public class RedirectController {
    private final UrlShortenerService shortener;

    @PostMapping("/")
    @Operation(summary = "Создание перенаправления")
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization",
            schema = @Schema(type = "string", defaultValue = "Bearer "),
            required = true,
            description = "Токен авторизации"
    )
    public CreateRedirectResponse createRedirect(
            @RequestBody CreateRedirectRequest request,
            @AuthenticationPrincipal UserDetails1 userDetails
    ) {
        var shortUrl = shortener.shorten(request.longUrl(), userDetails.getUserId());
        return new CreateRedirectResponse(shortUrl);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение информации по перенаправлению")
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization",
            schema = @Schema(type = "string", defaultValue = "Bearer "),
            required = true,
            description = "Токен авторизации"
    )
    public GetStatsResponse getStats(
            @PathVariable long id,
            @AuthenticationPrincipal UserDetails1 userDetails
    ) {
        var redirection = shortener.getRedirection(id, userDetails.getUserId());
        return new GetStatsResponse(redirection.getCreationDate(), redirection.getUsageCount());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление перенаправления")
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization",
            schema = @Schema(type = "string", defaultValue = "Bearer "),
            required = true,
            description = "Токен авторизации"
    )
    public void deleteRedirection(
            @PathVariable long id,
            @AuthenticationPrincipal UserDetails1 userDetails
    ) {
        shortener.deleteRedirection(id, userDetails.getUserId());
    }
}
