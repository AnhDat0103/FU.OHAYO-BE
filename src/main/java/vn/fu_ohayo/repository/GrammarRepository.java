package vn.fu_ohayo.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Grammar;
import vn.fu_ohayo.entity.Lesson;

import java.util.Optional;

@Repository
public interface GrammarRepository extends JpaRepository<Grammar, Integer> {

    int countAllByLessonAndDeletedIsFalse(Lesson lesson);

    Page<Grammar> findAllByLessonAndDeletedIsFalse(Lesson lesson, Pageable pageable);

    Optional<Grammar> findByTitleJpAndLesson(String titleJp, Lesson lesson);

    boolean existsByTitleJpAndMeaningAndLessonAndGrammarIdNot(String titleJp, String meaning, Lesson lesson, int grammarId);
}
