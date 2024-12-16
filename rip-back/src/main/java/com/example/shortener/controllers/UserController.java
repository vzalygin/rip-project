package com.example.shortener.controllers;

import com.example.shortener.messages.AuthenticateUserRequest;
import com.example.shortener.messages.LoginResponse;
import com.example.shortener.model.ModelException;
import com.example.shortener.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.logging.log4j.util.Strings.isEmpty;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Регистрация нового пользователя")
    ResponseEntity<?> registerUser(@RequestBody AuthenticateUserRequest authenticateUserRequest) {
        var username = authenticateUserRequest.username();
        var password = authenticateUserRequest.password();

        if (isEmpty(username) || isEmpty(password)) {
            return ResponseEntity.badRequest().body(new ModelException("Invalid input"));
        }

        userService.register(username, password);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    @Operation(summary = "Получение токена авторизации для пользователя")
    LoginResponse login(@RequestBody AuthenticateUserRequest authenticationRequest) throws Exception {
        var token = userService.authenticate(authenticationRequest.username(), authenticationRequest.password());
        return new LoginResponse(token);
    }
}
