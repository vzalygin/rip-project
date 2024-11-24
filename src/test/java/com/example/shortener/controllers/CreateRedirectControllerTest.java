package com.example.shortener.controllers;


import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import com.example.shortener.messages.CreateRedirectRequest;
import com.example.shortener.messages.CreateRedirectResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CreateRedirectControllerTest {
    private final String longUrl = "https://yandex.ru";
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void redirect() throws Exception {
        CreateRedirectResponse resp = makeShorterRequest(new CreateRedirectRequest(longUrl));
        this.mockMvc.perform(get(resp.getShortUrl()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", longUrl));
    }

    @Test
    void duplicate_vipLink() throws Exception {
        String vipKey = "duplicate";
        for (int expectedCode : new int[]{200, 400}) {
            expectMakeShorterRequestResponseCode(
                    String.format("{\"longUrl\":\"%s\", \"vipKey\": \"%s\"}", longUrl, vipKey),
                    expectedCode);
        }
    }

    @ParameterizedTest
    @MethodSource("provideTimeToLive")
    void test_timeToLive(String vipKey, long timeToLive, TimeUnit timeToLiveUnit, int expectedCode) throws Exception {
        expectMakeShorterRequestResponseCode(
                String.format("""
                         {"longUrl":"%s", "vipKey": "%s", "timeToLive":%d, "timeToLiveUnit":"%s"}
                        """, longUrl, vipKey, timeToLive, timeToLiveUnit.toString()),
                expectedCode);
    }

    private static Stream<Arguments> provideTimeToLive() {
        int vipKeyIncremented = 0;
        return Stream.of(
                Arguments.of(String.valueOf(++vipKeyIncremented), 0, SECONDS, 200),
                Arguments.of(String.valueOf(++vipKeyIncremented), 48, MINUTES, 200),
                Arguments.of(String.valueOf(++vipKeyIncremented), 12, HOURS, 200),
                Arguments.of(String.valueOf(++vipKeyIncremented), 2, DAYS, 200),

                Arguments.of(String.valueOf(++vipKeyIncremented), HOURS.toSeconds(48) + 1, SECONDS, 400),
                Arguments.of(String.valueOf(++vipKeyIncremented), HOURS.toMinutes(48) + 1, MINUTES, 400),
                Arguments.of(String.valueOf(++vipKeyIncremented), -1, SECONDS, 400),
                Arguments.of(String.valueOf(++vipKeyIncremented), -1, MINUTES, 400),
                Arguments.of(String.valueOf(++vipKeyIncremented), -4, HOURS, 400),
                Arguments.of(String.valueOf(++vipKeyIncremented), -3, DAYS, 400),
                Arguments.of(String.valueOf(++vipKeyIncremented), 49, HOURS, 400),
                Arguments.of(String.valueOf(++vipKeyIncremented), 3, DAYS, 400)
        );
    }

    private CreateRedirectResponse makeShorterRequest(String json) throws Exception {
        MvcResult mvcResult = expectMakeShorterRequestResponseCode(json, HttpStatus.OK.value());
        return mapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CreateRedirectResponse.class);
    }

    private MvcResult expectMakeShorterRequestResponseCode(String json, int expectedCode) throws Exception {
        return this.mockMvc.perform(post("/make_shorter")
                        .content(json)
                        .header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(status().is(expectedCode)).andReturn();
    }

    private CreateRedirectResponse makeShorterRequest(CreateRedirectRequest request) throws Exception {
        return makeShorterRequest(mapper.writeValueAsString(request));
    }

}