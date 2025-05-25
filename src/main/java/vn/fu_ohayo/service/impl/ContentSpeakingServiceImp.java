package vn.fu_ohayo.service.impl;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.ContentSpeakingRequest;
import vn.fu_ohayo.entity.Content;
import vn.fu_ohayo.entity.ContentSpeaking;
import vn.fu_ohayo.enums.ContentTypeEnum;
import vn.fu_ohayo.repository.ContentRepository;
import vn.fu_ohayo.repository.ContentSpeakingRepository;
import vn.fu_ohayo.service.ContentService;
import vn.fu_ohayo.service.ContentSpeakingService;

import java.util.List;

@Service
public class ContentSpeakingServiceImp implements ContentSpeakingService {
    private final ContentService contentService;
    private final ContentSpeakingService contentSpeakingService;
    private ContentRepository contentRepository;
    private ContentSpeakingRepository contentSpeakingRepository;

    public ContentSpeakingServiceImp(ContentRepository contentRepository, ContentService contentService, ContentSpeakingService contentSpeakingService) {
        this.contentRepository = contentRepository;
        this.contentService = contentService;
        this.contentSpeakingService = contentSpeakingService;
    }

    @Override
    public List<ContentSpeaking> getAllContentSpeakings() {
        return contentSpeakingRepository.findAll();
    }

    @Override
    public ContentSpeaking getContentSpeakingById(long id) {
        return contentSpeakingRepository.findById(id).orElse(null);
    }

    @Override
    public ContentSpeaking handleCreateContentSpeaking(ContentSpeakingRequest contentSpeakingRequest) {
        Content newContent = Content.builder()
                .contentType(ContentTypeEnum.Speaking)
                .build();
        ContentSpeaking contentSpeaking = ContentSpeaking.builder()
                .image(contentSpeakingRequest.getImage())
                .title(contentSpeakingRequest.getTitle())
                .category(contentSpeakingRequest.getCategory())
                .content(newContent)
                .build();
        return contentSpeakingRepository.save(contentSpeaking);
    }

    @Override
    public void deleteContentSpeakingById(long id) {
       contentSpeakingRepository.deleteById(id);
    }

    @Override
    public ContentSpeaking getContentSpeakingByContent(Content content) {
        return contentSpeakingRepository.findByContent(content);
    }

    @Override
    public ContentSpeaking handleSaveContentSpeaking(ContentSpeaking contentSpeaking) {
        return contentSpeakingRepository.save(contentSpeaking);
    }


}
