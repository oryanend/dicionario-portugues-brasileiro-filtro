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
  String baseUrl = "/api/v1/status";

  @Test
  public void getStatusShouldReturnStatusCodeOK() throws Exception {
    ResultActions result = mockMvc.perform(get(baseUrl).accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.updated_at").exists())
        .andExpect(jsonPath("$.dependencies").exists());
  }

  @Test
  public void getStatusShouldReturnDependenciesDatabase() throws Exception {
    ResultActions result = mockMvc.perform(get(baseUrl).accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(jsonPath("$.dependencies.database.status").value("healthy"))
        .andExpect(jsonPath("$.dependencies.database.version").value("2.3.232"))
        .andExpect(jsonPath("$.dependencies.database.max_connections").isNumber())
        .andExpect(jsonPath("$.dependencies.database.opened_connections").isNumber())
        .andExpect(jsonPath("$.dependencies.database.latency").exists())
        .andExpect(jsonPath("$.dependencies.database.latency.first_query").isNumber())
        .andExpect(jsonPath("$.dependencies.database.latency.second_query").isNumber())
        .andExpect(jsonPath("$.dependencies.database.latency.third_query").isNumber());
  }

  @Test
  public void getStatusShouldReturnDependenciesWebserver() throws Exception {
    ResultActions result = mockMvc.perform(get(baseUrl).accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(jsonPath("$.dependencies.webserver.status").value("healthy"))
        .andExpect(jsonPath("$.dependencies.webserver.version").value("3.5.7"))
        .andExpect(jsonPath("$.dependencies.webserver.provider").exists())
        .andExpect(jsonPath("$.dependencies.webserver.environment").exists())
        .andExpect(jsonPath("$.dependencies.webserver.last_commit_date").exists())
        .andExpect(jsonPath("$.dependencies.webserver.last_commit_sha").exists())
        .andExpect(jsonPath("$.dependencies.webserver.last_commit_author").exists())
        .andExpect(jsonPath("$.dependencies.webserver.last_commit_message").exists());
  }
}
