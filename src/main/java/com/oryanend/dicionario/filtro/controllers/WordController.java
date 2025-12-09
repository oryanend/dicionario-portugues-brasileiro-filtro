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
      @RequestParam(required = false, defaultValue = "0") int maxChar,
      @RequestParam(required = false, defaultValue = "0") int minChar,
      @RequestParam(required = false, defaultValue = "0") int charSize) {
    WordDTO dto = new WordDTO(wordService.getWord(minChar, maxChar, charSize));
    return ResponseEntity.ok().body(dto);
  }
}
