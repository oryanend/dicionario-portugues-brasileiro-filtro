package com.oryanend.dicionario.filtro.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class StatusControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  public void getStatusShouldReturnStatusCodeOK() throws Exception {
    ResultActions result = mockMvc.perform(get("/status").accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.updated_at").exists())
        .andExpect(jsonPath("$.dependencies.database.status").value("healthy"))
        .andExpect(jsonPath("$.dependencies.database.version").value("2.3.232"))
        .andExpect(jsonPath("$.dependencies.database.max_connections").isNumber())
        .andExpect(jsonPath("$.dependencies.database.opened_connections").isNumber());
  }
}
