package vn.fu_ohayo.service.impl;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.ContentSpeakingRequest;
import vn.fu_ohayo.dto.response.ContentSpeakingResponse;
import vn.fu_ohayo.entity.Content;
import vn.fu_ohayo.entity.ContentSpeaking;
import vn.fu_ohayo.enums.ContentTypeEnum;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ContentMapper;
import vn.fu_ohayo.repository.ContentSpeakingRepository;
import vn.fu_ohayo.service.ContentSpeakingService;

import java.util.List;

@Service
public class ContentSpeakingServiceImp implements ContentSpeakingService {
    private final ContentSpeakingRepository contentSpeakingRepository;
    private final ContentMapper contentMapper;

    public ContentSpeakingServiceImp(ContentSpeakingRepository contentSpeakingRepository, ContentMapper contentMapper) {
        this.contentSpeakingRepository = contentSpeakingRepository;
        this.contentMapper = contentMapper;
    }

    @Override
    public List<ContentSpeaking> getAllContentSpeakings() {
        return contentSpeakingRepository.findAll();
    }

    @Override
    public ContentSpeaking getContentSpeakingById(long id) throws AppException {
        return contentSpeakingRepository.findById(id).orElseThrow(() -> new AppException(ErrorEnum.INVALID_CONTENT_SPEAKING));
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
    public ContentSpeakingResponse updatePutContentSpeaking(long id, ContentSpeakingRequest request) {
        ContentSpeaking contentSpeaking = getContentSpeakingById(id);
        if(contentSpeaking != null) {
             contentSpeakingRepository.save(contentMapper.contentSpeakingRequestToContentSpeaking(contentSpeaking, request));
        }
        return contentMapper.toContentSpeakingResponse(contentSpeaking);
    }
    @Override
    public ContentSpeakingResponse updatePatchContentSpeaking(long id, ContentSpeakingRequest request) {
        ContentSpeaking contentSpeaking = getContentSpeakingById(id);
        if(contentSpeaking != null){
            if (request.getTitle() != null) {
                contentSpeaking.setTitle(request.getTitle());
            }
            if (request.getImage() != null) {
                contentSpeaking.setImage(request.getImage());
            }
            if (request.getCategory() != null) {
                contentSpeaking.setCategory(request.getCategory());
            }
            contentSpeakingRepository.save(contentSpeaking);
        }
        return contentMapper.toContentSpeakingResponse(contentSpeaking);
    }


}
