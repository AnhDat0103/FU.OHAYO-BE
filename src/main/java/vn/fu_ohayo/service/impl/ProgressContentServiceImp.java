package vn.fu_ohayo.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.entity.ContentReading;
import vn.fu_ohayo.entity.ProgressContent;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.entity.ContentListening;
import vn.fu_ohayo.repository.ProgressContentRepository;
import vn.fu_ohayo.service.ProgressContentService;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProgressContentServiceImp implements ProgressContentService {
    private final ProgressContentRepository progressContentRepository;

    @Override
    public ProgressContent saveUserListeningProgress(User user, ContentListening contentListening, int correct_answers, int total_questions) {
        ProgressContent progressContent = new ProgressContent();
        progressContent.setUser(user);
        progressContent.setContent(contentListening.getContent());
        progressContent.setCorrectAnswers(correct_answers);
        progressContent.setTotalQuestions(total_questions);
        return progressContentRepository.save(progressContent);
    }

    @Override
    public ProgressContent saveUserReadingProgress(User user, ContentReading contentReading, int progress) {
        ProgressContent progressContent = new ProgressContent();
        progressContent.setUser(user);
        progressContent.setContent(contentReading.getContent());
        progressContent.setProgressId(progress);
        return progressContentRepository.save(progressContent);
    }
}