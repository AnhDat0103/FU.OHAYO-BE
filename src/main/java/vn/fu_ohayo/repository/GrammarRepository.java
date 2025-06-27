package vn.fu_ohayo.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Grammar;
import vn.fu_ohayo.entity.Lesson;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrammarRepository extends JpaRepository<Grammar, Integer> {

    Optional<Grammar> findByTitleJp(String titleJp);

    boolean existsByTitleJpAndMeaningAndGrammarIdNot(String titleJp, String meaning, int grammarId);

    @Query("SELECT g FROM Grammar g JOIN g.lessons l " +
            "WHERE l.lessonId = :lessonId AND g.deleted = false")
    Page<Grammar> findAllByLessonIdAndDeletedIsFalse(@Param("lessonId") int lessonId, Pageable pageable);

}
