package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.request.ContentSpeakingRequest;
import vn.fu_ohayo.dto.response.ContentSpeakingResponse;
import vn.fu_ohayo.entity.Content;
import vn.fu_ohayo.entity.ContentSpeaking;

import java.util.List;

public interface ContentSpeakingService {
    List<ContentSpeaking> getAllContentSpeakings();

    ContentSpeaking getContentSpeakingById(long id);

    ContentSpeaking handleCreateContentSpeaking(ContentSpeakingRequest contentSpeakingRequest);

    void deleteContentSpeakingById(long id);

    void deleteContentSpeakingByIdLastly(long id);

    ContentSpeaking getContentSpeakingByContent(Content content);

    ContentSpeakingResponse updatePutContentSpeaking(long id, ContentSpeakingRequest contentSpeakingRequest);

    ContentSpeakingResponse updatePatchContentSpeaking(long id, ContentSpeakingRequest contentSpeakingRequest);

    Page<ContentSpeakingResponse> getContentSpeakingPage(int page, int size);

    ContentSpeakingResponse acceptContentSpeaking(long id);

    ContentSpeakingResponse rejectContentSpeaking(long id);

}
