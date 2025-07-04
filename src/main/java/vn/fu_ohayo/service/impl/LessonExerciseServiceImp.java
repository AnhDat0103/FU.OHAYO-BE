package vn.fu_ohayo.service.impl;

import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.AnswerQuestionRequest;
import vn.fu_ohayo.dto.request.ExerciseQuestionRequest;
import vn.fu_ohayo.dto.request.LessonExerciseRequest;
import vn.fu_ohayo.dto.response.ExerciseQuestionResponse;
import vn.fu_ohayo.dto.response.LessonExerciseResponse;
import vn.fu_ohayo.entity.AnswerQuestion;
import vn.fu_ohayo.entity.ExerciseQuestion;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.LessonExercise;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ExerciseQuestionMapper;
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
    private final ExerciseQuestionMapper exerciseQuestionMapper;

    public LessonExerciseServiceImp(LessonRepository lessonRepository, ExerciseQuestionRepository exerciseQuestionRepository,
                                    AnswerQuestionRepository answerQuestionRepository,
                                    LessonExerciseRepository lessonExerciseRepository, ExerciseQuestionMapper exerciseQuestionMapper) {
        this.lessonRepository = lessonRepository;
        this.exerciseQuestionRepository = exerciseQuestionRepository;
        this.answerQuestionRepository = answerQuestionRepository;
        this.lessonExerciseRepository = lessonExerciseRepository;
        this.exerciseQuestionMapper = exerciseQuestionMapper;
    }


    public Lesson handleGetLessonById(int lessonId) {
        return lessonRepository.findById(lessonId).orElseThrow(() -> new AppException(ErrorEnum.LESSON_NOT_FOUND));
    }


    @Override
    public Page<LessonExerciseResponse> getAllContentByLesson(int page, int size, int lessonId) {
        Lesson lesson = handleGetLessonById(lessonId);
        Page<LessonExercise> lessonExercises = lessonExerciseRepository.findAllByLesson(lesson, PageRequest.of(page, size));
        return lessonExercises.map(le -> {
            List<ExerciseQuestion> exerciseQuestions = exerciseQuestionRepository.findAllByLessonExercise(le);
            List<ExerciseQuestionResponse> exerciseQuestionResponses = new ArrayList<>();
            if (!exerciseQuestions.isEmpty()) {
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
        Lesson lesson = handleGetLessonById(lessonExerciseRequest.getLessonId());
        LessonExercise lessonExercise = lessonExerciseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.EXERCISE_NOT_FOUND));
        lessonExercise.setTitle(lessonExerciseRequest.getTitle());
        lessonExercise.setDuration(lessonExerciseRequest.getDuration());
        lessonExercise.setLesson(lesson);
        lessonExercise = lessonExerciseRepository.save(lessonExercise);
        List<ExerciseQuestionResponse> exerciseQuestionResponses = new ArrayList<>();
        if (lessonExerciseRequest.getContent() != null && !lessonExerciseRequest.getContent().isEmpty()) {
            List<ExerciseQuestion> existingQuestions = exerciseQuestionRepository.findAllByLessonExercise(lessonExercise);
            //delete existing exercise questions and their answers
            for (ExerciseQuestion question : existingQuestions) {
                List<AnswerQuestion> oldAnswers = answerQuestionRepository.findByExerciseQuestion(question);
                answerQuestionRepository.deleteAll(oldAnswers);
            }
            exerciseQuestionRepository.deleteAll(existingQuestions);
            //create new exercise questions and answers
            for (ExerciseQuestionRequest questionRequest : lessonExerciseRequest.getContent()) {
                List<AnswerQuestionRequest> answerRequests = questionRequest.getAnswerQuestions();
                int countCorrectAnswers = 0;
                for (AnswerQuestionRequest answerRequest : answerRequests) {
                    if (Boolean.TRUE.equals(answerRequest.getIsCorrect())) {
                        countCorrectAnswers++;
                    }
                }
                if (countCorrectAnswers != 1) {
                    throw new AppException(ErrorEnum.INVALID_ANSWER_CORRECT_COUNT);
                }
                //create exercise question and save it
                ExerciseQuestion exerciseQuestion = ExerciseQuestion.builder()
                        .questionText(questionRequest.getQuestionText())
                        .lessonExercise(lessonExercise)
                        .build();
                exerciseQuestion = exerciseQuestionRepository.save(exerciseQuestion);
                //create answer questions and save them
                for (AnswerQuestionRequest answerRequest : answerRequests) {
                    AnswerQuestion answerQuestion = AnswerQuestion.builder()
                            .answerText(answerRequest.getAnswerText())
                            .isCorrect(answerRequest.getIsCorrect())
                            .exerciseQuestion(exerciseQuestion)
                            .build();
                    answerQuestionRepository.save(answerQuestion);
                }
                // add to response
                exerciseQuestionResponses.add(ExerciseQuestionResponse.builder()
                        .exerciseQuestionId(exerciseQuestion.getExerciseQuestionId())
                        .questionText(exerciseQuestion.getQuestionText())
                        .createdAt(exerciseQuestion.getCreatedAt())
                        .updatedAt(exerciseQuestion.getUpdatedAt())
                        .answerQuestions(answerQuestionRepository.findAllByExerciseQuestion(exerciseQuestion))
                        .build());
            }
        } else {
            List<ExerciseQuestion> exerciseQuestions = exerciseQuestionRepository.findAllByLessonExercise(lessonExercise);
            for (ExerciseQuestion question : exerciseQuestions) {
                ExerciseQuestionResponse ex = ExerciseQuestionResponse.builder()
                        .exerciseQuestionId(question.getExerciseQuestionId())
                        .questionText(question.getQuestionText())
                        .createdAt(question.getCreatedAt())
                        .updatedAt(question.getUpdatedAt())
                        .answerQuestions(answerQuestionRepository.findAllByExerciseQuestion(question))
                        .build();
                exerciseQuestionResponses.add(ex);
            }
        }
        return LessonExerciseResponse.builder()
                .id(lessonExercise.getExerciseId())
                .title(lessonExercise.getTitle())
                .duration(lessonExercise.getDuration())
                .lessonId(lesson.getLessonId())
                .content(exerciseQuestionResponses)
                .build();
    }

    @Override
    public LessonExerciseResponse createExerciseLesson(LessonExerciseRequest lessonExerciseRequest) {
        Lesson lesson = handleGetLessonById(lessonExerciseRequest.getLessonId());
        // Validate that there is at least one question with one correct answer
        LessonExercise lessonExercise = LessonExercise.builder()
                .title(lessonExerciseRequest.getTitle())
                .duration(lessonExerciseRequest.getDuration())
                .lesson(lesson)
                .build();
        // Check if content is provided
        lessonExercise = lessonExerciseRepository.save(lessonExercise);
        List<ExerciseQuestionResponse> exerciseQuestionResponses = new ArrayList<>();
        // Validate that there is at least one question with one correct answer
        if (lessonExerciseRequest.getContent() != null) {
            // Check if there is at least one question with one correct answer
            for (ExerciseQuestionRequest exerciseQuestionRequest : lessonExerciseRequest.getContent()) {
                ExerciseQuestion exerciseQuestion = ExerciseQuestion.builder()
                        .questionText(exerciseQuestionRequest.getQuestionText())
                        .lessonExercise(lessonExercise)
                        .build();
                exerciseQuestion = exerciseQuestionRepository.save(exerciseQuestion);
                List<AnswerQuestionRequest> answerQuestionRequests = exerciseQuestionRequest.getAnswerQuestions();
                for (AnswerQuestionRequest answerQuestionRequest : answerQuestionRequests) {
                    AnswerQuestion answerQuestion = AnswerQuestion.builder()
                            .answerText(answerQuestionRequest.getAnswerText())
                            .isCorrect(answerQuestionRequest.getIsCorrect())
                            .exerciseQuestion(exerciseQuestion)
                            .build();
                    answerQuestionRepository.save(answerQuestion);
                }
                // Add to response
                exerciseQuestionResponses.add(ExerciseQuestionResponse.builder()
                        .exerciseQuestionId(exerciseQuestion.getExerciseQuestionId())
                        .questionText(exerciseQuestion.getQuestionText())
                        .createdAt(exerciseQuestion.getCreatedAt())
                        .updatedAt(exerciseQuestion.getUpdatedAt())
                        .answerQuestions(answerQuestionRepository.findAllByExerciseQuestion(exerciseQuestion))
                        .build());
            }
        }
        // If no content is provided, return an empty response
        return LessonExerciseResponse.builder()
                .id(lessonExercise.getExerciseId())
                .title(lessonExercise.getTitle())
                .duration(lessonExercise.getDuration())
                .lessonId(lesson.getLessonId())
                .content(exerciseQuestionResponses)
                .build();
    }

    @Override
    public void deleteExerciseLesson(int id) {
        answerQuestionRepository.deleteAllByExerciseId(id);
        exerciseQuestionRepository.deleteByExerciseId(id);
        lessonExerciseRepository.deleteById(id);
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
                    .status(e.getStatus())
                    .answerQuestions(answerQuestionRepository.findAllByExerciseQuestion(e))
                    .build();
        });

    }

    @Override
    public void handleSaveExerciseQuestionIntoLesson(Long lessonId, Long exerciseQuestionId) {
        exerciseQuestionRepository.saveExerciseQuestionIntoLessonId(lessonId, exerciseQuestionId);
    }

    @Override
    public void handleDeleteExerciseQuestionFromLesson(Long lessonId, Long exerciseQuestionId) {
        exerciseQuestionRepository.removeExerciseQuestionInLessonId(exerciseQuestionId, lessonId);
    }

    @Override
    public Page<ExerciseQuestionResponse> getAllExerciseQuestions(Long lessonId, int page, int size) {
        Page<ExerciseQuestion> exerciseQuestions = exerciseQuestionRepository.findAllAvailableExerciseQuestions(lessonId, PageRequest.of(page, size));
        return exerciseQuestions.map(exerciseQuestionMapper::toExerciseQuestionResponse);
    }

    public LessonExercise getLessonExerciseById(int id) {
        return lessonExerciseRepository.findById(id).orElseThrow(() -> new AppException(ErrorEnum.EXERCISE_NOT_FOUND));
    }
}
