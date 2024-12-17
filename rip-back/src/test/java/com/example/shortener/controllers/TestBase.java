package com.example.shortener.controllers;

import com.example.shortener.messages.CreateRedirectResponse;
import com.example.shortener.messages.LoginResponse;
import com.example.shortener.model.Redirection;
import com.example.shortener.model.User;
import com.example.shortener.repo.RedirectionRepo;
import com.example.shortener.repo.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestBase {
    private static final long USER_ID = 1;
    public static final String USER_USERNAME = "user";
    public static final String USER_PASSWORD = "password";
    public static final String LONG_URL = "https://yandex.ru";

    @Autowired
    public MockMvc mockMvc;
    @Autowired
    public ObjectMapper mapper;

    private String userToken;
    private User user;
    private Redirection redirection;

    @MockitoBean
    public UserRepo userRepo;
    @MockitoBean
    public RedirectionRepo redirectionRepo;

    @BeforeEach
    void init() {
        reset(userRepo, redirectionRepo);
        when(userRepo.save(any())).thenAnswer(answer((User user) -> {
            user.setId(1L);
            this.user = user;
            return user;
        }));
        when(redirectionRepo.save(any())).thenAnswer(answer((Redirection redirection) -> {
            redirection.setId(1L);
            this.redirection = redirection;
            return redirection;
        }));
    }

    public void whenUserRegistered() throws Exception {
        var body = Map.of(
                "username", USER_USERNAME,
                "password", USER_PASSWORD
        );

        mockMvc
                .perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andReturn();

        when(userRepo.findByUsername(USER_USERNAME)).thenReturn(Optional.of(user));
    }

    public LoginResponse whenUserLoggedIn() throws Exception {
        whenUserRegistered();

        var body = Map.of(
                "username", USER_USERNAME,
                "password", USER_PASSWORD
        );

        var response = mapper.readValue(mockMvc
                .perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), LoginResponse.class);

        userToken = response.token();
        return response;
    }

    public CreateRedirectResponse whenUserHasRedirect() throws Exception {
        whenUserLoggedIn();

        var body = Map.of(
                "longUrl", LONG_URL
        );

        var response = mapper.readValue(mockMvc
                .perform(withAuth(post("/redirection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body))))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), CreateRedirectResponse.class);

        when(redirectionRepo.findByShortKey(redirection.getShortKey())).thenReturn(Optional.ofNullable(redirection));
        when(redirectionRepo.findById(redirection.getId(), user.getId())).thenReturn(Optional.ofNullable(redirection));
        when(redirectionRepo.findAllByUserId(user.getId())).thenReturn(List.of(redirection));

        return response;
    }

    public MockHttpServletRequestBuilder withAuth(MockHttpServletRequestBuilder builders) {
        builders.header("Authorization", String.format("Bearer %s", userToken));
        return builders;
    }
}
