package com.example.lms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest//(classes = { AccountControllerV4.class })
//@WebMvcTest(AccountControllerV4.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles({ "test" })
@Slf4j
class AccountControllerV4Test {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCreateAccount() throws Exception {
        String requestJson =
            "{\"type\":\"MCA_ACCOUNT\",\"data\":{\"agree_term_and_condition\":true,\"agree_aqad\":true,\"onboarding_from_nest\":false,\"agree_shariah\":true}}";

        // Perform the POST request and verify the response
        mockMvc
            .perform(
                post("/4/deposit-accounts") // Mock JWT token
                    .contentType(MediaType.APPLICATION_JSON)
                        .principal(() -> "msms")
                    .content(requestJson)
            )
            .andExpect(status().isOk());
    }
}
