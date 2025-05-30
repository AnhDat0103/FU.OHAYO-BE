package vn.fu_ohayo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.ContentReadingRequest;
import vn.fu_ohayo.dto.response.ContentReadingResponse;
import vn.fu_ohayo.entity.Content;
import vn.fu_ohayo.entity.ContentReading;
import vn.fu_ohayo.enums.CategoryReadingEnum;
import vn.fu_ohayo.enums.ContentTypeEnum;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ContentMapper;
import vn.fu_ohayo.repository.ContentReadingRepository;
import vn.fu_ohayo.service.ContentReadingService;

import java.util.Arrays;

@Service
public class ContentReadingServiceImp implements ContentReadingService {

    private final ContentReadingRepository contentReadingRepository;
    private final ContentMapper contentMapper;

    public ContentReadingServiceImp(ContentReadingRepository contentReadingRepository, ContentMapper contentMapper) {
        this.contentReadingRepository = contentReadingRepository;
        this.contentMapper = contentMapper;
    }


    @Override
    public ContentReading getContentReadingById(Long id) {
        return contentReadingRepository.findById(id).orElse(null);
    }

    @Override
    public ContentReading handleCreateContentReading(ContentReadingRequest contentReadingRequest) {
//        if (Arrays.stream(CategoryReadingEnum.values())
//                .noneMatch(e -> e.name().equals(contentReadingRequest.getCategory()))) {
//            throw new AppException(ErrorEnum.INVALID_CATEGORY_CONTENT_READING);
//        }
        Content newContent = Content.builder()
                .contentType(ContentTypeEnum.Reading)
                .build();
        ContentReading contentReading = ContentReading.builder()
                .content(newContent)
                .image(contentReadingRequest.getImage())
                .title(contentReadingRequest.getTitle())
                .category(contentReadingRequest.getCategory())
                .audioFile(contentReadingRequest.getAudioFile())
                .timeNew(contentReadingRequest.getTimeNew())
                .scriptJp(contentReadingRequest.getScriptJp())
                .scriptVn(contentReadingRequest.getScriptVn())
                .build();
        return contentReadingRepository.save(contentReading);
    }

    @Override
    public void deleteContentReadingById(Long id) {
      contentReadingRepository.deleteById(id);
    }

    @Override
    public ContentReadingResponse updatePatchContentReading(long id, ContentReadingRequest request) {
        ContentReading contentReading = contentReadingRepository.findById(id).orElse(null);
        if (contentReading != null) {
            if(request.getImage() != null) {
                contentReading.setImage(request.getImage());
            }
            if(request.getTitle() != null) {
                contentReading.setTitle(request.getTitle());
            }
            if(request.getCategory() != null) {
                contentReading.setCategory(request.getCategory());
            }
            if(request.getAudioFile() != null) {
                contentReading.setAudioFile(request.getAudioFile());
            }
            if(request.getTimeNew() != null) {
                contentReading.setTimeNew(request.getTimeNew());
            }
            if(request.getScriptJp() != null) {
                contentReading.setScriptJp(request.getScriptJp());
            }
            if(request.getScriptVn() != null) {
                contentReading.setScriptVn(request.getScriptVn());
            }
            contentReadingRepository.save(contentReading);
        }
        return contentMapper.toContentReadingResponse(contentReading);
    }

    @Override
    public Page<ContentReadingResponse> getContentReadingPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ContentReading> prs = contentReadingRepository.findAll(pageable);
        Page<ContentReadingResponse> responsePage = prs.map(contentMapper::toContentReadingResponse);
        return responsePage;    }
}
