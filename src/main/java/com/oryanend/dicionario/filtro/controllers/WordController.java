package com.oryanend.dicionario.filtro.controllers;

import com.oryanend.dicionario.filtro.dto.WordDTO;
import com.oryanend.dicionario.filtro.services.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/words")
public class WordController {

  @Autowired private WordService wordService;

  @GetMapping
  public ResponseEntity<WordDTO> getRandomWord(
      @RequestParam(required = false, defaultValue = "0") int maxChar) {
    if (maxChar > 0) {
      WordDTO dto = new WordDTO(wordService.getRandomWordByMaxCharSize(maxChar));
      return ResponseEntity.ok().body(dto);
    }
    WordDTO dto = new WordDTO(wordService.getRandomWord());
    return ResponseEntity.ok().body(dto);
  }
}
