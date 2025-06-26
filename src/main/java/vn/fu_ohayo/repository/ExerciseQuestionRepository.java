package vn.fu_ohayo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.ContentListening;
import vn.fu_ohayo.entity.ExerciseQuestion;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.LessonExercise;

import java.util.List;

@Repository
public interface ExerciseQuestionRepository extends JpaRepository<ExerciseQuestion, Integer> {
    Page<ExerciseQuestion> findAllByContentListening(ContentListening contentListening, Pageable page);
    List<ExerciseQuestion> findAllByLessonExercise(LessonExercise lessonExercise);
    Page<ExerciseQuestion> findAllByLessonExercise(LessonExercise lessonExercise, Pageable pageable);
    List<ExerciseQuestion> findByContentListening(ContentListening contentListening);
}
