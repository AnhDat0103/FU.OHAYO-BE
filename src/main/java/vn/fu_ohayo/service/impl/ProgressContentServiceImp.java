package vn.fu_ohayo.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.entity.ProgressContent;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.entity.ContentListening;
import vn.fu_ohayo.repository.ProgressContentRepository;
import vn.fu_ohayo.service.ProgressContentService;

@Service
@AllArgsConstructor
public class ProgressContentServiceImp implements ProgressContentService {
    private final ProgressContentRepository progressContentRepository;

    @Override
    public ProgressContent saveUserProgress(User user, ContentListening contentListening, int progress) {
        ProgressContent progressContent = new ProgressContent();
        progressContent.setUser(user);
        progressContent.setContent(contentListening.getContent());
        progressContent.setProgressId(progress);
        return progressContentRepository.save(progressContent);
    }
}