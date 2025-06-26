package vn.fu_ohayo.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.fu_ohayo.entity.AnswerQuestion;
import vn.fu_ohayo.entity.ContentListening;
import vn.fu_ohayo.entity.ExerciseQuestion;
import vn.fu_ohayo.entity.ProgressContent;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ProgressStatus;
import vn.fu_ohayo.dto.request.ExerciseQuestionRequest;
import vn.fu_ohayo.repository.AnswerQuestionRepository;
import vn.fu_ohayo.repository.ContentListeningRepository;
import vn.fu_ohayo.repository.ExerciseQuestionRepository;
import vn.fu_ohayo.repository.ProgressContentRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.ContentListeningProgressService;
import vn.fu_ohayo.dto.request.AnswerQuestionRequest;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ContentUserListeningServiceImp implements ContentListeningProgressService {

    private final ExerciseQuestionRepository exerciseQuestionRepository;
    private final AnswerQuestionRepository answerQuestionRepository;
    private final ProgressContentRepository progressContentRepository;
    private final UserRepository userRepository;
    private final ContentListeningRepository contentListeningRepository;

    @Override
    public ProgressContent saveListeningProgress(Long userId, Long contentListeningId,
                                                 List<ExerciseQuestionRequest> userAnswers) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        ContentListening contentListening = contentListeningRepository.findById(contentListeningId)
                .orElseThrow(() -> new EntityNotFoundException("Content listening not found"));

        // Get all questions for this content
        List<ExerciseQuestion> questions = exerciseQuestionRepository
                .findByContentListening(contentListening);

        int totalQuestions = questions.size();
        int correctAnswers = 0;

        // Calculate correct answers
        for (ExerciseQuestionRequest userAnswer : userAnswers) {
            ExerciseQuestion question = exerciseQuestionRepository.findById(userAnswer.getQuestionId())
                    .orElseThrow(() -> new EntityNotFoundException("Question not found"));

            // Get correct answer for this question
            List<AnswerQuestion> correctAnswer = answerQuestionRepository
                    .findByExerciseQuestion_ExerciseQuestionIdAndIsCorrect(question.getExerciseQuestionId(), true);

            boolean hasCorrectAnswer = false;
            if (userAnswer.getAnswers() != null) {
                for (AnswerQuestionRequest answer : userAnswer.getAnswers()) {
                    for (AnswerQuestion correctAns : correctAnswer) {
                        if (answer.getAnswerId() == correctAns.getAnswerId() &&
                                Boolean.TRUE.equals(answer.getIsCorrect())) {
                            hasCorrectAnswer = true;
                            break;
                        }
                    }
                    if (hasCorrectAnswer) break;
                }
            }
            if (hasCorrectAnswer) {
                correctAnswers++;
            }
        }

        // Check if progress already exists
        ProgressContent progressContent = progressContentRepository
                .findByUser_UserIdAndContent_ContentId(userId, contentListening.getContent().getContentId())
                .orElse(new ProgressContent());

        progressContent.setUser(user);
        progressContent.setContent(contentListening.getContent());
        progressContent.setCorrectAnswers(correctAnswers);
        progressContent.setTotalQuestions(totalQuestions);
        progressContent.setProgressStatus(correctAnswers == totalQuestions ?
                ProgressStatus.COMPLETED : ProgressStatus.IN_PROGRESS);

        return progressContentRepository.save(progressContent);
    }
}