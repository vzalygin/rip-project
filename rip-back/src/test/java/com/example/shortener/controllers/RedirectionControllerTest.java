package com.example.shortener.controllers;


import com.example.shortener.messages.GetStatsResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RedirectionControllerTest extends TestBase {

    @Test
    void makeRedirection() throws Exception {
        whenUserLoggedIn();

        var body = Map.of(
                "longUrl", LONG_URL
        );

        mockMvc.perform(withAuth(post("/redirection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.shortUrl").hasJsonPath())
                .andReturn();

        verify(redirectionRepo, times(1)).save(any());
    }

    @Test
    void redirect() throws Exception {
        var url = whenUserHasRedirect().shortUrl().substring(21);

        mockMvc.perform(get(url))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", LONG_URL));
    }

    @Test
    void getRedirectsStats() throws Exception {
        var id = Long.toString(whenUserHasRedirect().id());

        mapper.readValue(mockMvc.perform(withAuth(get(String.format("/redirection/%s", id))))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), GetStatsResponse.class);
    }

    @Test
    void getRedirectionList() throws Exception {
        whenUserHasRedirect();

        var response = mapper.readValue(mockMvc.perform(withAuth(get("/redirection/all")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), GetStatsResponse[].class);

        Assertions.assertEquals(1, response.length);
    }

}
