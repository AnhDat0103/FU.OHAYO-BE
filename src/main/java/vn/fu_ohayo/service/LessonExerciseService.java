package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.fu_ohayo.dto.request.LessonExerciseRequest;
import vn.fu_ohayo.dto.response.ExerciseQuestionResponse;
import vn.fu_ohayo.dto.response.LessonExerciseResponse;
import vn.fu_ohayo.entity.LessonExercise;

public interface LessonExerciseService {

    Page<LessonExerciseResponse> getAllContentByLesson(int page, int size, int lessonId);

    LessonExerciseResponse updateExerciseLesson(int id, LessonExerciseRequest lessonExerciseRequest) ;

    LessonExerciseResponse createExerciseLesson(LessonExerciseRequest lessonExerciseRequest);

    void deleteExerciseLesson(int id);

    Page<ExerciseQuestionResponse> getExerciseQuestionByExerciseLesson(int page, int size, int exerciseId);

    LessonExercise getLessonExerciseById(int id);

}
