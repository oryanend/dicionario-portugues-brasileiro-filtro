package com.oryanend.dicionario.filtro.repositories;

import com.oryanend.dicionario.filtro.entities.Word;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

  @Query(value = "SELECT * FROM words ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
  Word findRandomWord();

  @Query(
      value = "SELECT * FROM words w WHERE LENGTH(w.word) <= :maxChar ORDER BY RANDOM() LIMIT 1",
      nativeQuery = true)
  Optional<Word> findRandomWordByMaxCharSize(@Param("maxChar") int maxChar);
}
