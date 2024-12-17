package com.example.shortener.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest extends TestBase {
    @Test
    void successfullyRegister() throws Exception {
        var body = Map.of(
                "username", USER_USERNAME,
                "password", USER_PASSWORD
        );

        mockMvc
                .perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk());
        verify(userRepo, times(1)).save(any());
    }

    @Test
    void unsuccessfullyRegisterTooShortPassword() throws Exception {
        var body = Map.of(
                "username", "user",
                "password", "pass"
        );

        mockMvc
                .perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
        verify(userRepo, times(0)).save(any());
    }

    @Test
    void unsuccessfullyRegisterEmptyField() throws Exception {
        var body = Map.of(
                "username", "",
                "password", "pass"
        );

        mockMvc
                .perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
        verify(userRepo, times(0)).save(any());
    }

    @Test
    void unsuccessfullyRegisterDuplicatedUsername() throws Exception {
        whenUserRegistered();

        var body = Map.of(
                "username", USER_USERNAME,
                "password", USER_PASSWORD
        );

        mockMvc
                .perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(userRepo, times(1)).save(any());
    }

    @Test
    void successfullyLogin() throws Exception {
        whenUserRegistered();

        var body = Map.of(
                "username", USER_USERNAME,
                "password", USER_PASSWORD
        );

        mockMvc
                .perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").hasJsonPath())
                .andReturn();
    }
}
