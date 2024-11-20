package com.example.fiap.archburgers.adapters.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class IndexApiHandlerTest {
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        IndexApiHandler indexApiHandler = new IndexApiHandler();
        mockMvc = MockMvcBuilders.standaloneSetup(indexApiHandler).build();
    }

    @Test
    void testIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void testHealthcheck() throws Exception {
        mockMvc.perform(get("/healthcheck"))
                .andExpect(status().isOk());
    }
}