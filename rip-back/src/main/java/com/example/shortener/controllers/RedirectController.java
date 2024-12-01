package com.example.shortener.controllers;

import com.example.shortener.messages.CreateRedirectRequest;
import com.example.shortener.messages.CreateRedirectResponse;
import com.example.shortener.services.UrlShortenerService;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@AllArgsConstructor
public class RedirectController extends BaseController {

    private final UrlShortenerService shortener;

    @RequestMapping(value="/make-shorter", method = RequestMethod.POST)
    public CreateRedirectResponse createRedirect(
            @RequestBody CreateRedirectRequest request
    ) {
        Pair<String, String> shortUrlAndSecret = shortener.shorten(request.getLongUrl());
        return new CreateRedirectResponse(shortUrlAndSecret.getFirst(), shortUrlAndSecret.getSecond());
    }

    @RequestMapping("/{shortKey}")
    public void doRedirect(@PathVariable String shortKey, HttpServletResponse response) {
        var redirection = shortener.resolve(shortKey);
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", redirection.getLongUrl());
    }
}
