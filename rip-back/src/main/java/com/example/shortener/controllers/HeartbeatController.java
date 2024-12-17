package com.example.shortener.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeartbeatController {
    @GetMapping("/ping")
    @Operation(summary = "Проверка на работоспособность")
    public String ping() {
        return "pong";
    }
}
