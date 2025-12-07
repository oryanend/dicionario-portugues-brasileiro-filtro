package com.oryanend.dicionario.filtro.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.transaction.Transactional;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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
public class WordControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  public void getWordsShouldReturnStatusCodeOK() throws Exception {
    ResultActions result = mockMvc.perform(get("/words").accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.word").isNotEmpty())
        .andExpect(jsonPath("$.word").isString())
        .andExpect(jsonPath("$.word").exists());
  }

  @Test
  public void getWordsWithSizeShouldReturnWordSorted() throws Exception {
    int wordSize = 6;

    ResultActions result =
        mockMvc.perform(get("/words?wordSize=" + wordSize).accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.word").isNotEmpty())
        .andExpect(jsonPath("$.word").isString())
        .andExpect(jsonPath("$.word").exists())
        .andExpect(jsonPath("$.word", hasLengthLessOrEqualTo(wordSize)));
  }

  @Test
  public void getWordsSizeWhenSizeNotFoundReturnStatusCodeNotFound() throws Exception {
    int wordSize = 2;

    ResultActions result =
        mockMvc.perform(get("/words?wordSize=" + wordSize).accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Resource not found"))
        .andExpect(
            jsonPath("$.message")
                .value("Não foi possível encontrar uma palavra com o tamanho máximo especificado."))
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.path").value("/words"))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  private Matcher<Object> hasLengthLessOrEqualTo(int max) {
    return new TypeSafeMatcher<>() {
      @Override
      protected boolean matchesSafely(Object item) {
        return item.toString().length() <= max;
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("a string with length <= " + max);
      }
    };
  }
}
