package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.AnswerQuestion;
import vn.fu_ohayo.entity.ExerciseQuestion;

import java.util.List;

@Repository
public interface AnswerQuestionRepository extends JpaRepository<AnswerQuestion, Integer> {
    List<AnswerQuestion> findByExerciseQuestion(ExerciseQuestion exerciseQuestion);
}
