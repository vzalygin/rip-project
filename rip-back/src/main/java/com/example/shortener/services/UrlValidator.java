package com.example.shortener.services;


import com.example.shortener.model.InvalidUrlException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class UrlValidator {

    String validateAndGetError(String url) {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        try {
            HttpResponse<Void> response = client.send(
                    HttpRequest.newBuilder(new URI(url)).build(),
                    HttpResponse.BodyHandlers.discarding());
            int responseCode = response.statusCode();

            if (responseCode >= HttpServletResponse.SC_BAD_REQUEST) {
                throw new InvalidUrlException(String.format("invalid response from url = %s", url));
            }
        } catch (IOException | URISyntaxException | InterruptedException | IllegalArgumentException e) {
            throw new InvalidUrlException(String.format("invalid url = %s", url));
        }

        return null;
    }

}
