package com.oryanend.dicionario.filtro.services;

import com.oryanend.dicionario.filtro.entities.Word;
import com.oryanend.dicionario.filtro.repositories.WordRepository;
import com.oryanend.dicionario.filtro.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WordService {

  @Autowired private WordRepository repository;

  @Transactional(readOnly = true)
  public Word getRandomWord() {
    return repository.findRandomWord();
  }

  @Transactional(readOnly = true)
  public Word getRandomWordByMaxCharSize(int maxChar) {
    return repository
        .findRandomWordByMaxCharSize(maxChar)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "Não foi possível encontrar uma palavra com o tamanho máximo especificado."));
  }

  @Transactional(readOnly = true)
  public Word getRandomWordByMinCharSize(int minChar) {
    return repository
        .findRandomWordByMinCharSize(minChar)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "Não foi possível encontrar uma palavra com o tamanho máximo especificado."));
  }
}
