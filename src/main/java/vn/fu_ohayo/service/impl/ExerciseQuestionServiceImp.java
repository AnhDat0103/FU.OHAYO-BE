package vn.fu_ohayo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.AnswerQuestionRequest;
import vn.fu_ohayo.dto.request.ExerciseQuestionRequestForListening;
import vn.fu_ohayo.dto.response.AnswerQuestionResponse;
import vn.fu_ohayo.dto.response.ExerciseQuestionResponse;
import vn.fu_ohayo.entity.AnswerQuestion;
import vn.fu_ohayo.entity.ContentListening;
import vn.fu_ohayo.entity.ExerciseQuestion;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ExerciseQuestionMapper;
import vn.fu_ohayo.repository.AnswerQuestionRepository;
import vn.fu_ohayo.repository.ExerciseQuestionRepository;
import vn.fu_ohayo.service.ContentListeningService;
import vn.fu_ohayo.service.ExerciseQuestionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExerciseQuestionServiceImp implements ExerciseQuestionService {
    private final ExerciseQuestionRepository exerciseQuestionRepository;
    private final AnswerQuestionRepository answerQuestionRepository ;
    private final ExerciseQuestionMapper exerciseQuestionMapper;
    private final ContentListeningService contentListeningService;

    public ExerciseQuestionServiceImp(ExerciseQuestionRepository exerciseQuestionRepository,
                                      AnswerQuestionRepository answerQuestionRepository, ExerciseQuestionMapper exerciseQuestionMapper, ContentListeningService contentListeningService) {
        this.exerciseQuestionRepository = exerciseQuestionRepository;
        this.answerQuestionRepository = answerQuestionRepository;
        this.exerciseQuestionMapper = exerciseQuestionMapper;
        this.contentListeningService = contentListeningService;

    }

    @Override
    public Page<ExerciseQuestionResponse> getExerciseQuestionPage(int page, int size, long contentListeningId) {
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

//    @Override
    public ExerciseQuestionResponse handleCreateExerciseQuestion(ExerciseQuestionRequestForListening ExerciseQuestionRequestForListening) {
        List<AnswerQuestionRequest> answerRequests = ExerciseQuestionRequestForListening.getAnswerQuestionRequests();
        int correctCount = 0;
        for (AnswerQuestionRequest answerRequest : answerRequests) {
            if (Boolean.TRUE.equals(answerRequest.getIsCorrect())) {
                correctCount++;
            }
        }

        if (correctCount != 1) {
            throw new AppException(ErrorEnum.INVALID_ANSWER_CORRECT_COUNT); // bạn cần tự định nghĩa ErrorEnum này
        }
        ExerciseQuestion exerciseQuestion = ExerciseQuestion.builder()
                .questionText(ExerciseQuestionRequestForListening.getQuestionText())
                .contentListening(contentListeningService.getContentListeningById(ExerciseQuestionRequestForListening.getContent_listening_id()))
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
    public ExerciseQuestionResponse updatePatchExerciseQuestion(int id, ExerciseQuestionRequestForListening ExerciseQuestionRequestForListening) {
        // Tìm ExerciseQuestion
        ExerciseQuestion exerciseQuestion = exerciseQuestionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.QUESTION_NOT_FOUND));

        // Cập nhật questionText nếu có
        if (ExerciseQuestionRequestForListening.getQuestionText() != null) {
            exerciseQuestion.setQuestionText(ExerciseQuestionRequestForListening.getQuestionText());
        }

        // Xóa tất cả answer hiện tại
        List<AnswerQuestion> oldAnswers = answerQuestionRepository.findByExerciseQuestion(exerciseQuestion);
        for (AnswerQuestion oldAnswer : oldAnswers) {
            answerQuestionRepository.deleteById(oldAnswer.getAnswerId());
        }
        oldAnswers.clear();


        // Thêm lại các answer mới từ request
        List<AnswerQuestionRequest> answerRequests = ExerciseQuestionRequestForListening.getAnswerQuestionRequests();

        for (AnswerQuestionRequest answerRequest : answerRequests) {
            AnswerQuestion newAnswer = AnswerQuestion.builder()
                    .answerText(answerRequest.getAnswerText())
                    .isCorrect(answerRequest.getIsCorrect())
                    .exerciseQuestion(exerciseQuestion)
                    .build();
            answerQuestionRepository.save(newAnswer);
        }
        return exerciseQuestionMapper.toExerciseQuestionResponse(exerciseQuestion);
    }

    @Override
    public List<ExerciseQuestionResponse> handleCreateAllExerciseQuestion(List<ExerciseQuestionRequestForListening> ExerciseQuestionRequestForListenings) {
        List<ExerciseQuestionResponse> responses = new ArrayList<>();
        for (ExerciseQuestionRequestForListening ExerciseQuestionRequestForListening : ExerciseQuestionRequestForListenings) {
            List<AnswerQuestionRequest> answerRequests = ExerciseQuestionRequestForListening.getAnswerQuestionRequests();

            List<AnswerQuestion> answerQuestionSet = new ArrayList<>();
            for (AnswerQuestionRequest answerRequest : answerRequests) {
                AnswerQuestion answerQuestion = AnswerQuestion.builder()
                        .answerText(answerRequest.getAnswerText())
                        .isCorrect(answerRequest.getIsCorrect())
                        .build();
                answerQuestionSet.add(answerQuestion);
            }
            ExerciseQuestion exerciseQuestion = ExerciseQuestion.builder()
                    .questionText(ExerciseQuestionRequestForListening.getQuestionText())
                    .contentListening(contentListeningService.getContentListeningById(ExerciseQuestionRequestForListening.getContent_listening_id()))
                    .answerQuestions(answerQuestionSet)
                    .build();
            exerciseQuestion = exerciseQuestionRepository.save(exerciseQuestion);
            responses.add(exerciseQuestionMapper.toExerciseQuestionResponse(exerciseQuestion));
        }
        return responses;
    }

}
