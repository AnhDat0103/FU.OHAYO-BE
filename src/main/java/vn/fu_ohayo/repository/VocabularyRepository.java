package vn.fu_ohayo.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Grammar;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.Vocabulary;

import java.util.Collection;
import java.util.List;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Integer> {

    @Query("SELECT v FROM Vocabulary v JOIN v.lessons l WHERE l.lessonId = :lessonId")
    Page<Vocabulary> findAllByLessonId(@Param("lessonId") int lessonId, Pageable pageable);

    @Query("SELECT COUNT(v) > 0 FROM Vocabulary v JOIN v.lessons l " +
            "WHERE v.kanji = :kanji AND v.kana = :kana AND v.meaning = :meaning AND l.lessonId = :lessonId")
    boolean existsByKanjiAndKanaAndMeaningAndLessonId(
            @Param("kanji") String kanji,
            @Param("kana") String kana,
            @Param("meaning") String meaning,
            @Param("lessonId") int lessonId
    );

    @Query("SELECT COUNT(v) > 0 FROM Vocabulary v JOIN v.lessons l " +
            "WHERE v.kanji = :kanji AND v.kana = :kana AND v.meaning = :meaning " +
            "AND l.lessonId = :lessonId AND v.vocabularyId <> :vocabularyId")
    boolean existsDuplicateVocabularyInLessonExceptId(
            @Param("kanji") String kanji,
            @Param("kana") String kana,
            @Param("meaning") String meaning,
            @Param("lessonId") int lessonId,
            @Param("vocabularyId") int vocabularyId
    );


    Vocabulary findAllByKanjiAndKanaAndMeaning( String kanji, String kana,String meaning);
}