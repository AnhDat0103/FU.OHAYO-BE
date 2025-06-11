package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.entity.ExerciseResult;
import java.util.List;

@Repository
public interface ExerciseResultRepository extends JpaRepository<ExerciseResult, Long> {
    List<ExerciseResult> findAllByUserAndLessonExerciseIn(User user, List<LessonExercise> lessonExercises);
     int countByUserAndLessonExerciseIn(User user, List<LessonExercise> lessonExercises);
    @Query(value = "SELECT er FROM ExerciseResult er " +
            "WHERE er.user = :user " +
            "AND er.lessonExercise IN :lessonExercises " +
            "ORDER BY er.submissionTime DESC " +
            "FETCH FIRST :size ROWS ONLY", nativeQuery = false)
    List<ExerciseResult> findAllByUserAndTopOnReviewAndExerciseIn(User user,List<LessonExercise> lessonExercises, int size);
}
