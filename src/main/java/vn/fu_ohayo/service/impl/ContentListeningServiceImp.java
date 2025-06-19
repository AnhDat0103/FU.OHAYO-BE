package vn.fu_ohayo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.ContentListeningRequest;
import vn.fu_ohayo.dto.response.ContentListeningResponse;
import vn.fu_ohayo.entity.Content;
import vn.fu_ohayo.entity.ContentListening;
import vn.fu_ohayo.entity.ProgressContent;
import vn.fu_ohayo.enums.ContentTypeEnum;
import vn.fu_ohayo.mapper.ContentMapper;
import vn.fu_ohayo.repository.ContentListeningRepository;
import vn.fu_ohayo.service.ContentListeningService;

import java.util.List;

@Service
public class ContentListeningServiceImp implements ContentListeningService {
    private final ContentListeningRepository contentListeningRepository;
    private final ContentMapper contentMapper;

    public ContentListeningServiceImp(ContentListeningRepository contentListeningRepository, ContentMapper contentMapper) {
        this.contentListeningRepository = contentListeningRepository;
        this.contentMapper = contentMapper;
    }


    @Override
    public ContentListening getContentListeningById(Long id) {
        ContentListening l = contentListeningRepository.findBycontentListeningId(id);
        return l;
    }

    @Override
    public ContentListening handleCreateContentListening(ContentListeningRequest contentListeningRequest) {
        Content newContent = Content.builder()
                .contentType(ContentTypeEnum.Listening)
                .build();
        ContentListening contentListening = ContentListening.builder()
                .content(newContent)
                .image(contentListeningRequest.getImage())
                .title(contentListeningRequest.getTitle())
                .category(contentListeningRequest.getCategory())
                .audioFile(contentListeningRequest.getAudioFile())
                .scriptJp(contentListeningRequest.getScriptJp())
                .scriptVn(contentListeningRequest.getScriptVn())
                .build();
        return contentListeningRepository.save(contentListening);
    }

    @Override
    public void deleteContentListeningById(Long id) {
        contentListeningRepository.deleteById(id);
    }

    @Override
    public ContentListeningResponse updatePatchContentListening(long id, ContentListeningRequest request) {
        ContentListening contentListening = contentListeningRepository.findBycontentListeningId(id);
        if (contentListening != null) {
            if(request.getImage() != null) {
                contentListening.setImage(request.getImage());
            }
            if(request.getTitle() != null) {
                contentListening.setTitle(request.getTitle());
            }
            if(request.getCategory() != null) {
                contentListening.setCategory(request.getCategory());
            }
            if(request.getAudioFile() != null) {
                contentListening.setAudioFile(request.getAudioFile());
            }
            if(request.getScriptJp() != null) {
                contentListening.setScriptJp(request.getScriptJp());
            }
            if(request.getScriptVn() != null) {
                contentListening.setScriptVn(request.getScriptVn());
            }
            contentListeningRepository.save(contentListening);
        }
        return contentMapper.toContentListeningResponse(contentListening);
    }

    @Override
    public Page<ContentListeningResponse> getContentListeningPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ContentListening> prs = contentListeningRepository.findAll(pageable);
        Page<ContentListeningResponse> responsePage = prs.map(contentMapper::toContentListeningResponse);
        return responsePage;    }



}
