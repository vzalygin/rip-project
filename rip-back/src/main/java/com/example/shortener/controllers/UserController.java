package com.example.shortener.controllers;

import com.example.shortener.messages.GetStatsResponse;
import com.example.shortener.model.Redirection;
import com.example.shortener.services.UrlShortenerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController extends BaseController {

    private final UrlShortenerService service;

    @RequestMapping(value = "user/redirection/{id}", method = RequestMethod.GET)
    public GetStatsResponse getStats(@PathVariable long redirectionId) {
        Redirection redirection = service.(redirectionId);
        return new GetStatsResponse(redirection.getCreationDate(), redirection.getUsageCount());
    }

    @RequestMapping(value = "user/redirection/{secretKey}", method = RequestMethod.DELETE)
    public void deleteRedirection(@PathVariable String secretKey) {
        service.deleteRedirection(secretKey);
    }

}
