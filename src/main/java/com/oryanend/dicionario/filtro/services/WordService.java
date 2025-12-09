package com.oryanend.dicionario.filtro.services;

import com.oryanend.dicionario.filtro.entities.Word;
import com.oryanend.dicionario.filtro.repositories.WordRepository;
import com.oryanend.dicionario.filtro.services.exceptions.InvalidParameterException;
import com.oryanend.dicionario.filtro.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WordService {

  @Autowired private WordRepository repository;

  @Transactional(readOnly = true)
  public Word getWord(Integer minChar, Integer maxChar, Integer charSize) {
    if (minChar > 0 && maxChar > 0 && minChar > maxChar) {
      throw new InvalidParameterException(
          "O valor mínimo de caracteres não pode ser maior que o valor máximo");
    }
    if (charSize > 0) {
      if (minChar > 0 || maxChar > 0) {
        throw new InvalidParameterException(
            "Os valores não podem ser combinados, charSize deve ser usado sozinho");
      }
      return repository
          .findRandomWordByEqualCharSize(charSize)
          .orElseThrow(
              () ->
                  new ResourceNotFoundException(
                      "Não foi possível encontrar uma palavra com o tamanho especificado"));
    }
    if (minChar > 0 && maxChar > 0) {
      return repository
          .findRandomWordByMinAndMaxCharSize(minChar, maxChar)
          .orElseThrow(
              () ->
                  new ResourceNotFoundException(
                      "Não foi possível encontrar uma palavra com o tamanho máximo e mínimo especificado"));
    }
    if (minChar > 0) {
      return repository
          .findRandomWordByMinCharSize(minChar)
          .orElseThrow(
              () ->
                  new ResourceNotFoundException(
                      "Não foi possível encontrar uma palavra com o tamanho mínimo especificado"));
    }
    if (maxChar > 0) {
      return repository
          .findRandomWordByMaxCharSize(maxChar)
          .orElseThrow(
              () ->
                  new ResourceNotFoundException(
                      "Não foi possível encontrar uma palavra com o tamanho máximo especificado"));
    }
    return repository.findRandomWord();
  }
}
