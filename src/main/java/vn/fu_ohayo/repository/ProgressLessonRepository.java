package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.ProgressLesson;
import vn.fu_ohayo.entity.User;

@Repository
public interface ProgressLessonRepository extends JpaRepository<ProgressLesson, Integer> {
    ProgressLesson findByUserAndLesson(User user, Lesson lesson);
}
