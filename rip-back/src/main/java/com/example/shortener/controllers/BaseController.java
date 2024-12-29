package com.example.shortener.controllers;

import com.example.shortener.model.ErrorObject;
import com.example.shortener.model.ModelException;
import com.example.shortener.model.RedirectionNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

@ControllerAdvice
@RequiredArgsConstructor
public class BaseController {
    private final ObjectMapper mapper;

    @ExceptionHandler(RedirectionNotFoundException.class)
    void handle(HttpServletResponse response, RedirectionNotFoundException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))) {
            bw.write(mapper.writeValueAsString(new ErrorObject("Redirection not found by key " + exception.getMessage())));
        }
    }

    @ExceptionHandler(ModelException.class)
    void handleModelException(HttpServletResponse response, ModelException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))) {
            bw.write(mapper.writeValueAsString(new ErrorObject(exception.getMessage())));
        }
    }
}
