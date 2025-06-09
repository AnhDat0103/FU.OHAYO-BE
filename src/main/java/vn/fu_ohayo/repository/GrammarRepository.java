package vn.fu_ohayo.repository;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Grammar;
import vn.fu_ohayo.entity.Lesson;

import java.util.Optional;

@Repository
public interface GrammarRepository extends JpaRepository<Grammar, Integer> {

    int countAllByLesson(Lesson lesson);

    Page<Grammar> findAllByLesson(Lesson lesson, Pageable pageable);

    Optional<Grammar> findGrammarByTitleJpAndLesson(String titleJp, Lesson lesson);
}
