package vn.fu_ohayo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.ContentSpeakingRequest;
import vn.fu_ohayo.dto.response.ContentSpeakingResponse;
import vn.fu_ohayo.entity.Content;
import vn.fu_ohayo.entity.ContentSpeaking;
import vn.fu_ohayo.enums.ContentStatus;
import vn.fu_ohayo.enums.ContentTypeEnum;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ContentMapper;
import vn.fu_ohayo.repository.ContentSpeakingRepository;
import vn.fu_ohayo.service.ContentSpeakingService;
import vn.fu_ohayo.service.DialogueService;

import java.util.List;

@Service
public class ContentSpeakingServiceImp implements ContentSpeakingService {
    private final ContentSpeakingRepository contentSpeakingRepository;
    private final ContentMapper contentMapper;
    private final DialogueService dialogueService;

    public ContentSpeakingServiceImp(ContentSpeakingRepository contentSpeakingRepository, ContentMapper contentMapper, DialogueService dialogueService) {
        this.contentSpeakingRepository = contentSpeakingRepository;
        this.contentMapper = contentMapper;
        this.dialogueService = dialogueService;
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
                .status(contentSpeakingRequest.getStatus())
                .jlptLevel(contentSpeakingRequest.getJlptLevel())
                .content(newContent)
                .build();
        return contentSpeakingRepository.save(contentSpeaking);
    }

    @Override
    public void deleteContentSpeakingById(long id) {
      ContentSpeaking contentSpeaking = getContentSpeakingById(id);
      contentSpeaking.setDeleted(true);
      contentSpeakingRepository.save(contentSpeaking);
    }

    @Override
    public void deleteContentSpeakingByIdLastly(long id) {
        dialogueService.deleteDialogueByContenSpeaking(getContentSpeakingById(id));
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

    @Override
    public Page<ContentSpeakingResponse> getContentSpeakingPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
         Page<ContentSpeaking> prs = contentSpeakingRepository.findAllByDeleted(pageable, false);
        Page<ContentSpeakingResponse> responsePage = prs.map(contentMapper::toContentSpeakingResponse);
        return responsePage;
    }

    @Override
    public ContentSpeakingResponse acceptContentSpeaking(long id) {
        ContentSpeaking contentSpeaking = getContentSpeakingById(id);
        contentSpeaking.setStatus(ContentStatus.PUBLIC);
        contentSpeakingRepository.save(contentSpeaking);
        return contentMapper.toContentSpeakingResponse(contentSpeaking);
    }

}
