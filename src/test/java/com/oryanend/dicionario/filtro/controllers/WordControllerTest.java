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

  private static final String INVALID_PARAMS_ERROR_MESSAGE =
      "Apenas um parâmetro de filtro pode ser usado por vez (maxChar, minChar ou charSize).";

  @Test
  public void getWordsShouldReturnRandomWord() throws Exception {
    ResultActions result = mockMvc.perform(get("/words").accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.word").isNotEmpty())
        .andExpect(jsonPath("$.word").isString())
        .andExpect(jsonPath("$.word").exists());
  }

  @Test
  public void getWordsMaxCharShouldReturnWordLessOrEqualToMaxChar() throws Exception {
    int maxChar = 6;

    ResultActions result =
        mockMvc.perform(get("/words?maxChar=" + maxChar).accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.word").isNotEmpty())
        .andExpect(jsonPath("$.word").isString())
        .andExpect(jsonPath("$.word").exists())
        .andExpect(jsonPath("$.word", hasLengthLessOrEqualTo(maxChar)));
  }

  @Test
  public void getWordsMaxCharWhenMaxCharNotFoundShouldThrowNotFoundException() throws Exception {
    int maxChar = 2;

    ResultActions result =
        mockMvc.perform(get("/words?maxChar=" + maxChar).accept(MediaType.APPLICATION_JSON));

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

  @Test
  public void getWordsMinCharShouldReturnWordGreaterOrEqualToMinChar() throws Exception {
    int minChar = 7;

    ResultActions result =
        mockMvc.perform(get("/words?minChar=" + minChar).accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.word").isNotEmpty())
        .andExpect(jsonPath("$.word").isString())
        .andExpect(jsonPath("$.word").exists())
        .andExpect(jsonPath("$.word", greaterOrEqualLength(minChar)));
  }

  @Test
  public void getWordsMinCharWhenMinCharNotFoundShouldThrowNotFoundException() throws Exception {
    int minChar = 12;

    ResultActions result =
        mockMvc.perform(get("/words?minChar=" + minChar).accept(MediaType.APPLICATION_JSON));

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

  @Test
  public void getWordsCharSizeShouldReturnWordEqualToCharSize() throws Exception {
    int charSize = 7;

    ResultActions result =
        mockMvc.perform(get("/words?charSize=" + charSize).accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.word").isNotEmpty())
        .andExpect(jsonPath("$.word").isString())
        .andExpect(jsonPath("$.word").exists())
        .andExpect(jsonPath("$.word", equalLength(charSize)));
  }

  @Test
  public void getWordsCharSizeWhenCharSizeNotFoundShouldThrowNotFoundException() throws Exception {
    int charSize = 12;

    ResultActions result =
        mockMvc.perform(get("/words?charSize=" + charSize).accept(MediaType.APPLICATION_JSON));

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

  public static Matcher<String> equalLength(int length) {
    return new TypeSafeMatcher<>() {

      @Override
      public void describeTo(Description description) {
        description.appendText("string length equal to " + length);
      }

      @Override
      protected boolean matchesSafely(String item) {
        return item != null && item.length() == length;
      }
    };
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

  public static Matcher<String> greaterOrEqualLength(int minLength) {
    return new TypeSafeMatcher<>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("string length >= " + minLength);
      }

      @Override
      protected boolean matchesSafely(String item) {
        return item != null && item.length() >= minLength;
      }
    };
  }

  @Test
  public void getWordsWithMultipleParamsShouldThrowInvalidParameterException() throws Exception {
    ResultActions result =
        mockMvc.perform(get("/words?maxChar=5&minChar=3").accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Invalid parameter"))
        .andExpect(jsonPath("$.message").value(INVALID_PARAMS_ERROR_MESSAGE))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.path").value("/words"))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  public void getWordsWithAllParamsShouldThrowInvalidParameterException() throws Exception {
    ResultActions result =
        mockMvc.perform(
            get("/words?maxChar=5&minChar=3&charSize=4").accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Invalid parameter"))
        .andExpect(jsonPath("$.message").value(INVALID_PARAMS_ERROR_MESSAGE))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.path").value("/words"))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  public void getWordsWithMaxCharAndCharSizeShouldThrowInvalidParameterException()
      throws Exception {
    ResultActions result =
        mockMvc.perform(get("/words?maxChar=5&charSize=4").accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Invalid parameter"))
        .andExpect(jsonPath("$.message").value(INVALID_PARAMS_ERROR_MESSAGE))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.path").value("/words"))
        .andExpect(jsonPath("$.timestamp").exists());
  }
}
