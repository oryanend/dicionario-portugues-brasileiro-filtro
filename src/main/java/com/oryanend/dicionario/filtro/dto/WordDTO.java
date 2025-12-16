package com.oryanend.dicionario.filtro.dto;

import com.oryanend.dicionario.filtro.entities.Word;
import com.oryanend.dicionario.filtro.projections.WordProjection;

public class WordDTO {
  private String word;

  private WordDTO() {}

  public WordDTO(String word) {
    this.word = word;
  }

  public WordDTO(Word entity) {
    this.word = entity.getWord();
  }

  public WordDTO(WordProjection entity) {
    this.word = entity.getWord();
  }

  public String getWord() {
    return word;
  }
}
