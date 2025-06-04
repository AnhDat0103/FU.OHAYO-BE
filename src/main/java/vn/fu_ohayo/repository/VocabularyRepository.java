package vn.fu_ohayo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.Vocabulary;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Integer> {

    int countAllByLesson(Lesson lesson);

    Collection<Vocabulary> findAllByLesson(Lesson lesson);

    Page<Vocabulary> findAllByLesson(Pageable pageable, Lesson lesson);

    boolean existsByKanjiAndKanaAndMeaningAndLesson(String kanji,String kana, String meaning, Lesson lesson);

    boolean existsByKanjiAndKanaAndMeaningAndLessonAndVocabularyIdNot(String kanji, String kana, String meaning, Lesson lesson, int vocabularyId);
}