package vn.fu_ohayo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.AnswerQuestionRequest;
import vn.fu_ohayo.dto.request.ExerciseQuestionRequest;
import vn.fu_ohayo.dto.response.ExerciseQuestionResponse;
import vn.fu_ohayo.entity.AnswerQuestion;
import vn.fu_ohayo.entity.ContentListening;
import vn.fu_ohayo.entity.ExerciseQuestion;
import vn.fu_ohayo.entity.LessonExercise;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ExerciseQuestionMapper;
import vn.fu_ohayo.repository.AnswerQuestionRepository;
import vn.fu_ohayo.repository.ExerciseQuestionRepository;
import vn.fu_ohayo.service.ContentListeningService;
import vn.fu_ohayo.service.ExerciseQuestionService;
import vn.fu_ohayo.service.LessonExerciseService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExerciseQuestionServiceImp implements ExerciseQuestionService {
    private final ExerciseQuestionRepository exerciseQuestionRepository;
    private final AnswerQuestionRepository answerQuestionRepository;
    private final ExerciseQuestionMapper exerciseQuestionMapper;
    private final ContentListeningService contentListeningService;
    private final LessonExerciseService lessonExerciseService;

    public ExerciseQuestionServiceImp(ExerciseQuestionRepository exerciseQuestionRepository,
                                      AnswerQuestionRepository answerQuestionRepository, ExerciseQuestionMapper exerciseQuestionMapper, ContentListeningService contentListeningService, LessonExerciseService lessonExerciseService) {
        this.exerciseQuestionRepository = exerciseQuestionRepository;
        this.answerQuestionRepository = answerQuestionRepository;
        this.exerciseQuestionMapper = exerciseQuestionMapper;
        this.contentListeningService = contentListeningService;
        this.lessonExerciseService = lessonExerciseService;
    }

    @Override
    public Page<ExerciseQuestionResponse> getExerciseQuestionByContentListeingPage(int page, int size, long contentListeningId) {
        Pageable pageable = PageRequest.of(page, size);
        ContentListening contentListening = contentListeningService.getContentListeningById(contentListeningId);
        Page<ExerciseQuestion> prs = exerciseQuestionRepository.findAllByContentListening(contentListening, pageable);
        Page<ExerciseQuestionResponse> responsePage = prs.map(exerciseQuestionMapper::toExerciseQuestionResponse);
        return responsePage;
    }

    @Override
    public ExerciseQuestionResponse getExerciseQuestionById(int id) {
        ExerciseQuestion exerciseQuestion = exerciseQuestionRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorEnum.QUESTION_NOT_FOUND));
        return exerciseQuestionMapper.toExerciseQuestionResponse(exerciseQuestion);
    }

    @Override
    public ExerciseQuestionResponse handleCreateExerciseQuestion(ExerciseQuestionRequest exerciseQuestionRequest) {
        if (exerciseQuestionRequest.getExerciseId() == null && exerciseQuestionRequest.getContentListeningId() == null) {
            throw new AppException(ErrorEnum.EXIST_AT_LEAST_CONTENT_LISTENING_OR_EXERCISE);
        }
        List<AnswerQuestionRequest> answerRequests = exerciseQuestionRequest.getAnswerQuestions();
        int correctCount = 0;
        for (AnswerQuestionRequest answerRequest : answerRequests) {
            if (Boolean.TRUE.equals(answerRequest.getIsCorrect())) {
                correctCount++;
            }
        }
        if (correctCount != 1) {
            throw new AppException(ErrorEnum.INVALID_ANSWER_CORRECT_COUNT);
        }
        ExerciseQuestion exerciseQuestion = ExerciseQuestion.builder()
                .questionText(exerciseQuestionRequest.getQuestionText())
                .contentListening(contentListeningService.getContentListeningById(exerciseQuestionRequest.getContentListeningId()))
                .lessonExercise(lessonExerciseService.getLessonExerciseById(exerciseQuestionRequest.getExerciseId()))
                .build();
        exerciseQuestion = exerciseQuestionRepository.save(exerciseQuestion);
        for (AnswerQuestionRequest answerRequest : answerRequests) {
            AnswerQuestion answerQuestion = AnswerQuestion.builder()
                    .answerText(answerRequest.getAnswerText())
                    .isCorrect(answerRequest.getIsCorrect())
                    .exerciseQuestion(exerciseQuestion)
                    .build();
            answerQuestionRepository.save(answerQuestion);
        }
        return exerciseQuestionMapper.toExerciseQuestionResponse(exerciseQuestion);
    }

    @Override
    public void deleteExerciseQuestionById(int id) {
        exerciseQuestionRepository.deleteById(id);
    }

    @Override
    public ExerciseQuestionResponse updatePatchExerciseQuestion(int id, ExerciseQuestionRequest ExerciseQuestionRequest) {
        List<AnswerQuestionRequest> answerRequests = ExerciseQuestionRequest.getAnswerQuestions();
        int correctCount = 0;
        for (AnswerQuestionRequest answerRequest : answerRequests) {
            if (Boolean.TRUE.equals(answerRequest.getIsCorrect())) {
                correctCount++;
            }
        }

        if (correctCount != 1) {
            throw new AppException(ErrorEnum.INVALID_ANSWER_CORRECT_COUNT); // bạn cần tự định nghĩa ErrorEnum này
        }
        // Tìm ExerciseQuestion
        ExerciseQuestion exerciseQuestion = exerciseQuestionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.QUESTION_NOT_FOUND));

        // Cập nhật questionText nếu có
        if (ExerciseQuestionRequest.getQuestionText() != null) {
            exerciseQuestion.setQuestionText(ExerciseQuestionRequest.getQuestionText());
        }
//        exerciseQuestion.getAnswerQuestions().clear();

        // Xóa tất cả answer hiện tại
        List<AnswerQuestion> oldAnswers = answerQuestionRepository.findByExerciseQuestion(exerciseQuestion);
        for (AnswerQuestion oldAnswer : oldAnswers) {
            answerQuestionRepository.deleteById(oldAnswer.getAnswerId());
        }
        oldAnswers.clear();

        // Thêm lại các answer mới từ request
        answerRequests = ExerciseQuestionRequest.getAnswerQuestions();
        for (AnswerQuestionRequest answerRequest : answerRequests) {
            AnswerQuestion newAnswer = AnswerQuestion.builder()
                    .answerText(answerRequest.getAnswerText())
                    .isCorrect(answerRequest.getIsCorrect())
                    .exerciseQuestion(exerciseQuestion)
                    .build();
            answerQuestionRepository.save(newAnswer);
        }
        exerciseQuestion = exerciseQuestionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.QUESTION_NOT_FOUND));
        return exerciseQuestionMapper.toExerciseQuestionResponse(exerciseQuestion);
    }

    @Override
    public List<ExerciseQuestionResponse> handleCreateAllExerciseQuestion(List<ExerciseQuestionRequest> questionRequest) {
        List<ExerciseQuestionResponse> responses = new ArrayList<>();
        for (ExerciseQuestionRequest exerciseQuestionRequest : questionRequest) {
            List<AnswerQuestionRequest> answerRequests = exerciseQuestionRequest.getAnswerQuestions();

            List<AnswerQuestion> answerQuestionSet = new ArrayList<>();
            for (AnswerQuestionRequest answerRequest : answerRequests) {
                AnswerQuestion answerQuestion = AnswerQuestion.builder()
                        .answerText(answerRequest.getAnswerText())
                        .isCorrect(answerRequest.getIsCorrect())
                        .build();
                answerQuestionSet.add(answerQuestion);
            }
            ExerciseQuestion exerciseQuestion = ExerciseQuestion.builder()
                    .questionText(exerciseQuestionRequest.getQuestionText())
                    .contentListening(contentListeningService.getContentListeningById(exerciseQuestionRequest.getContentListeningId()))
                    .lessonExercise(lessonExerciseService.getLessonExerciseById(exerciseQuestionRequest.getExerciseId()))

                    .answerQuestions(answerQuestionSet)
                    .build();
            exerciseQuestion = exerciseQuestionRepository.save(exerciseQuestion);
            responses.add(exerciseQuestionMapper.toExerciseQuestionResponse(exerciseQuestion));
        }
        return responses;
    }

    @Override
    public Page<ExerciseQuestionResponse> getExerciseQuestionPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ExerciseQuestion> prs = exerciseQuestionRepository.findAll(pageable);
        Page<ExerciseQuestionResponse> responsePage = prs.map(exerciseQuestionMapper::toExerciseQuestionResponse);
        return responsePage;
    }

}
