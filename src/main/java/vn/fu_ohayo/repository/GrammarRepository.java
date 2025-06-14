package vn.fu_ohayo.repository;
import org.springframework.data.domain.Page;
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

    @Query("SELECT g FROM Grammar g JOIN g.favoriteGrammars fg WHERE fg.id = :folderId AND g.deleted = false")
    List<Grammar> findAllByFavoriteGrammarId(@Param("folderId") int folderId);

    @Query("SELECT COUNT(g) FROM Grammar g JOIN g.favoriteGrammars fg WHERE fg.id = :folderId")
    int countByFavoriteGrammarId(@Param("folderId") int folderId);

    int countAllByLessonAndDeletedIsFalse(Lesson lesson);

    Page<Grammar> findAllByLessonAndDeletedIsFalse(Lesson lesson, Pageable pageable);
    List<Grammar> findAllByLessonAndDeletedIsFalse(Lesson lesson);

    Optional<Grammar> findByTitleJpAndLesson(String titleJp, Lesson lesson);

    boolean existsByTitleJpAndMeaningAndLessonAndGrammarIdNot(String titleJp, String meaning, Lesson lesson, int grammarId);
}
