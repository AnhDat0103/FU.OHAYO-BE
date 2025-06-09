package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.entity.ExerciseResult;
import vn.fu_ohayo.enums.ProgressStatus;

import java.util.List;

@Repository
public interface ExerciseResultRepository extends JpaRepository<ExerciseResult, Long> {
    List<ExerciseResult> findAllByUserAndLessonExerciseIn(User user, List<LessonExercise> lessonExercises);
}
