package com.oryanend.dicionario.filtro.repositories;

import com.oryanend.dicionario.filtro.entities.Word;
import com.oryanend.dicionario.filtro.projections.WordProjection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

  @Query(value = "SELECT id, word FROM words ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
  WordProjection findRandomWord();

  @Query(
      value =
          "SELECT id, word FROM words w WHERE LENGTH(w.word) <= :maxChar ORDER BY RANDOM() LIMIT 1",
      nativeQuery = true)
  Optional<WordProjection> findRandomWordByMaxCharSize(@Param("maxChar") int maxChar);

  @Query(
      value =
          "SELECT id, word FROM words w WHERE LENGTH(w.word) >= :minChar ORDER BY RANDOM() LIMIT 1",
      nativeQuery = true)
  Optional<WordProjection> findRandomWordByMinCharSize(@Param("minChar") int minChar);

  @Query(
      value =
          "SELECT id, word FROM words w WHERE LENGTH(w.word) = :charSize ORDER BY RANDOM() LIMIT 1",
      nativeQuery = true)
  Optional<WordProjection> findRandomWordByEqualCharSize(@Param("charSize") int charSize);

  @Query(
      value =
          "SELECT id, word FROM words w "
              + "WHERE LENGTH(w.word) >= :minChar AND LENGTH(w.word) <= :maxChar "
              + "ORDER BY RANDOM() LIMIT 1",
      nativeQuery = true)
  Optional<WordProjection> findRandomWordByMinAndMaxCharSize(
      @Param("minChar") int minChar, @Param("maxChar") int maxChar);
}
