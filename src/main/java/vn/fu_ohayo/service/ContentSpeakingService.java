package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.request.ContentSpeakingRequest;
import vn.fu_ohayo.entity.Content;
import vn.fu_ohayo.entity.ContentSpeaking;

import java.util.List;

public interface ContentSpeakingService {
    List<ContentSpeaking> getAllContentSpeakings();
    ContentSpeaking getContentSpeakingById(long id);
    ContentSpeaking handleCreateContentSpeaking(ContentSpeakingRequest contentSpeakingRequest);
    void deleteContentSpeakingById(long id);
    ContentSpeaking getContentSpeakingByContent(Content content);
    ContentSpeaking handleSaveContentSpeaking(ContentSpeaking contentSpeaking);

}
