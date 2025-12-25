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
  String baseUrl = "/api/v1/words";

  @Test
  public void getWordsShouldReturnRandomWord() throws Exception {
    ResultActions result = mockMvc.perform(get(baseUrl).accept(MediaType.APPLICATION_JSON));

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
        mockMvc.perform(get(baseUrl + "?maxChar=" + maxChar).accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.word").isNotEmpty())
        .andExpect(jsonPath("$.word").isString())
        .andExpect(jsonPath("$.word").exists())
        .andExpect(jsonPath("$.word", hasLengthLessOrEqualTo(maxChar)));
  }

  @Test
  public void getWordsMaxCharWhenMaxCharNotFoundShouldThrowNotFoundException() throws Exception {
    int maxChar = 1;

    ResultActions result =
        mockMvc.perform(get(baseUrl + "?maxChar=" + maxChar).accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Resource not found"))
        .andExpect(
            jsonPath("$.message")
                .value("Não foi possível encontrar uma palavra com o tamanho máximo especificado"))
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.path").value(baseUrl))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  public void getWordsMinCharShouldReturnWordGreaterOrEqualToMinChar() throws Exception {
    int minChar = 7;

    ResultActions result =
        mockMvc.perform(get(baseUrl + "?minChar=" + minChar).accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.word").isNotEmpty())
        .andExpect(jsonPath("$.word").isString())
        .andExpect(jsonPath("$.word").exists())
        .andExpect(jsonPath("$.word", greaterOrEqualLength(minChar)));
  }

  @Test
  public void getWordsMinCharWhenMinCharNotFoundShouldThrowNotFoundException() throws Exception {
    int minChar = 120;

    ResultActions result =
        mockMvc.perform(get(baseUrl + "?minChar=" + minChar).accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Resource not found"))
        .andExpect(
            jsonPath("$.message")
                .value("Não foi possível encontrar uma palavra com o tamanho mínimo especificado"))
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.path").value(baseUrl))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  public void getWordsCharSizeShouldReturnWordEqualToCharSize() throws Exception {
    int charSize = 7;

    ResultActions result =
        mockMvc.perform(get(baseUrl + "?charSize=" + charSize).accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.word").isNotEmpty())
        .andExpect(jsonPath("$.word").isString())
        .andExpect(jsonPath("$.word").exists())
        .andExpect(jsonPath("$.word", equalLength(charSize)));
  }

  @Test
  public void getWordsCharSizeWhenCharSizeNotFoundShouldThrowNotFoundException() throws Exception {
    int charSize = 120;

    ResultActions result =
        mockMvc.perform(get(baseUrl + "?charSize=" + charSize).accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Resource not found"))
        .andExpect(
            jsonPath("$.message")
                .value("Não foi possível encontrar uma palavra com o tamanho especificado"))
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.path").value(baseUrl))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  public void getWordsWhenMinCharGreaterThanMaxCharShouldThrowInvalidParameterException()
      throws Exception {
    int minChar = 8;
    int maxChar = 5;

    ResultActions result =
        mockMvc.perform(
            get(baseUrl + "?minChar=" + minChar + "&maxChar=" + maxChar)
                .accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Invalid Param error"))
        .andExpect(
            jsonPath("$.message")
                .value("O valor mínimo de caracteres não pode ser maior que o valor máximo"))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.path").value(baseUrl))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  public void getWordsWhenCharSizeCombinedWithMinShouldThrowInvalidParameterException()
      throws Exception {
    int charSize = 5;
    int minChar = 3;

    ResultActions result =
        mockMvc.perform(
            get(baseUrl + "?charSize=" + charSize + "&minChar=" + minChar)
                .accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Invalid Param error"))
        .andExpect(
            jsonPath("$.message")
                .value("Os valores não podem ser combinados, charSize deve ser usado sozinho"))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.path").value(baseUrl))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  public void getWordsWhenCharSizeCombinedWithMaxShouldThrowInvalidParameterException()
      throws Exception {
    int charSize = 5;
    int maxChar = 7;

    ResultActions result =
        mockMvc.perform(
            get(baseUrl + "?charSize=" + charSize + "&maxChar=" + maxChar)
                .accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Invalid Param error"))
        .andExpect(
            jsonPath("$.message")
                .value("Os valores não podem ser combinados, charSize deve ser usado sozinho"))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.path").value(baseUrl))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  public void getWordsWhenCharSizeCombinedWithMinAndMaxShouldThrowInvalidParameterException()
      throws Exception {
    int charSize = 5;
    int minChar = 3;
    int maxChar = 7;

    ResultActions result =
        mockMvc.perform(
            get(baseUrl + "?charSize=" + charSize + "&minChar=" + minChar + "&maxChar=" + maxChar)
                .accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Invalid Param error"))
        .andExpect(
            jsonPath("$.message")
                .value("Os valores não podem ser combinados, charSize deve ser usado sozinho"))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.path").value(baseUrl))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  public void getWordsWithMinAndMaxCharShouldReturnWordBetweenMinAndMaxChar() throws Exception {
    int minChar = 4;
    int maxChar = 8;

    ResultActions result =
        mockMvc.perform(
            get(baseUrl + "?minChar=" + minChar + "&maxChar=" + maxChar)
                .accept(MediaType.APPLICATION_JSON));

    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.word").isNotEmpty())
        .andExpect(jsonPath("$.word").isString())
        .andExpect(jsonPath("$.word").exists())
        .andExpect(jsonPath("$.word", greaterOrEqualLength(minChar)))
        .andExpect(jsonPath("$.word", hasLengthLessOrEqualTo(maxChar)));
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
}
