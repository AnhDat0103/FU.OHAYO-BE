package vn.fu_ohayo.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.Subject;

import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    boolean existsByName(String name);

    Page<Lesson> findAllBySubject(Subject subject, Pageable pageable);

    Optional<Lesson> getLessonByLessonId(int lessonId);
}
