package com.whenthen.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whenthen.challenge.client.RestClient;
import com.whenthen.challenge.model.request.TicketRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestClient restClient;

    @Test
    @DisplayName("createSupportTicketAndCountPriorities, when request is valid, status code is 2xxSuccessful")
    void createSupportTicketAndCountPriorities_whenRequestIsValid_statusCodeIs2xxSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/tickets")
                .content(asJsonString(new TicketRequest("Saeed", "saeed@gmail.com", "subject", "test message")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}