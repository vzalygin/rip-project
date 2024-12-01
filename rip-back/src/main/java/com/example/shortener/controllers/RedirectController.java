package com.example.shortener.controllers;

import com.example.shortener.messages.CreateRedirectRequest;
import com.example.shortener.messages.CreateRedirectResponse;
import com.example.shortener.messages.GetStatsResponse;
import com.example.shortener.model.Redirection;
import com.example.shortener.security.UserDetails1;
import com.example.shortener.services.UrlShortenerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@AllArgsConstructor
public class RedirectController extends BaseController {

    private final UrlShortenerService shortener;

    @RequestMapping("/a/{shortKey}")
    public void doRedirect(@PathVariable String shortKey, HttpServletResponse response) {
        var redirection = shortener.resolve(shortKey);
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", redirection.getLongUrl());
    }

    @PostMapping(value="redirection")
    public CreateRedirectResponse createRedirect(
            @RequestBody CreateRedirectRequest request,
            @AuthenticationPrincipal UserDetails1 userDetails
    ) {
        var shortUrl = shortener.shorten(request.longUrl(), userDetails.getUserId());
        return new CreateRedirectResponse(shortUrl);
    }

    @GetMapping(value = "redirection/{id}")
    public GetStatsResponse getStats(
            @PathVariable long id,
            @AuthenticationPrincipal UserDetails1 userDetails
    ) {
        Redirection redirection = shortener.getRedirection(id, userDetails.getUserId());
        return new GetStatsResponse(redirection.getCreationDate(), redirection.getUsageCount());
    }

    @DeleteMapping(value = "redirection/{id}")
    public void deleteRedirection(
            @PathVariable long id,
            @AuthenticationPrincipal UserDetails1 userDetails
    ) {
        shortener.deleteRedirection(id, userDetails.getUserId());
    }
}
