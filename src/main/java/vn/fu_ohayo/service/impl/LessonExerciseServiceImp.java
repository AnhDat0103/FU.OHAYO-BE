package vn.fu_ohayo.service.impl;

import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.LessonExerciseRequest;
import vn.fu_ohayo.dto.response.AnswerQuestionResponse;
import vn.fu_ohayo.dto.response.ExerciseQuestionResponse;
import vn.fu_ohayo.dto.response.LessonExerciseResponse;
import vn.fu_ohayo.entity.AnswerQuestion;
import vn.fu_ohayo.entity.ExerciseQuestion;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.LessonExercise;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.repository.AnswerQuestionRepository;
import vn.fu_ohayo.repository.ExerciseQuestionRepository;
import vn.fu_ohayo.repository.LessonExerciseRepository;
import vn.fu_ohayo.repository.LessonRepository;
import vn.fu_ohayo.service.LessonExerciseService;

import java.util.ArrayList;
import java.util.List;

@Service
public class LessonExerciseServiceImp implements LessonExerciseService {

    private final LessonRepository lessonRepository;
    private final ExerciseQuestionRepository exerciseQuestionRepository;
    private final AnswerQuestionRepository answerQuestionRepository;
    private final LessonExerciseRepository lessonExerciseRepository;

    public LessonExerciseServiceImp(LessonRepository lessonRepository, ExerciseQuestionRepository exerciseQuestionRepository,
                                    AnswerQuestionRepository answerQuestionRepository,
                                    LessonExerciseRepository lessonExerciseRepository) {
        this.lessonRepository = lessonRepository;
        this.exerciseQuestionRepository = exerciseQuestionRepository;
        this.answerQuestionRepository = answerQuestionRepository;
        this.lessonExerciseRepository = lessonExerciseRepository;
    }


    public Lesson handleGetLessonById(int lessonId) {
        return lessonRepository.findById(lessonId).orElseThrow(() -> new AppException(ErrorEnum.LESSON_NOT_FOUND));
    }


    @Override
    public Page<LessonExerciseResponse> getAllContentByLesson(int page, int size, int lessonId) {
        Lesson lesson = handleGetLessonById(lessonId);
        Page<LessonExercise> lessonExercises = lessonExerciseRepository.findAllByLesson(lesson, PageRequest.of(page, size));
        return  lessonExercises.map(le-> {
                    List<ExerciseQuestion> exerciseQuestions = exerciseQuestionRepository.findAllByLessonExercise(le);
                    if(exerciseQuestions.isEmpty()) {
                        throw new AppException(ErrorEnum.EXERCISE_QUESTION_NOT_FOUND);
                    }
                    List<ExerciseQuestionResponse> exerciseQuestionResponses = new ArrayList<>();
                    for (ExerciseQuestion exerciseQuestion : exerciseQuestions) {
                        ExerciseQuestionResponse ex = ExerciseQuestionResponse.builder()
                                .exerciseQuestionId(exerciseQuestion.getExerciseQuestionId())
                                .questionText(exerciseQuestion.getQuestionText())
                                .updatedAt(exerciseQuestion.getUpdatedAt())
                                .createdAt(exerciseQuestion.getCreatedAt())
                                .answerQuestions(answerQuestionRepository.findAllByExerciseQuestion(exerciseQuestion))
                                .build();
                        exerciseQuestionResponses.add(ex);
                    }
            return LessonExerciseResponse.builder()
                    .id(le.getExerciseId())
                    .title(le.getTitle())
                    .content(exerciseQuestionResponses)
                    .lessonId(lessonId)
                    .duration(le.getDuration())
                    .build();
        });
    }

    @Override
    public LessonExerciseResponse updateExerciseLesson(int id, LessonExerciseRequest lessonExerciseRequest) {
        return null;
    }

    @Override
    public LessonExerciseResponse createExerciseLesson(LessonExerciseRequest lessonExerciseRequest) {
        return null;
    }

    @Override
    public void deleteExerciseLesson(int id) {

    }

    @Override
    public Page<ExerciseQuestionResponse> getExerciseQuestionByExerciseLesson(int page, int size, int exerciseId) {
        LessonExercise exercise = lessonExerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException(ErrorEnum.EXERCISE_NOT_FOUND));
        Page<ExerciseQuestion> exerciseQuestions = exerciseQuestionRepository.findAllByLessonExercise(exercise, PageRequest.of(page, size));
        return exerciseQuestions.map(e -> {
            return ExerciseQuestionResponse.builder()
                    .exerciseQuestionId(e.getExerciseQuestionId())
                    .questionText(e.getQuestionText())
                    .createdAt(e.getCreatedAt())
                    .updatedAt(e.getUpdatedAt())
                    .answerQuestions(answerQuestionRepository.findAllByExerciseQuestion(e))
                    .build();
        });

    }
}
