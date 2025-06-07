package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.request.ContentReadingRequest;
import vn.fu_ohayo.dto.response.ContentReadingGrammarResponse;
import vn.fu_ohayo.dto.response.ContentReadingResponse;
import vn.fu_ohayo.dto.response.ContentReadingVocabularyResponse;
import vn.fu_ohayo.entity.ContentReading;
import vn.fu_ohayo.entity.Grammar;
import vn.fu_ohayo.entity.Vocabulary;

public interface ContentReadingService {
    //    List<ContentReading> getAllContentReading() ;
//    ContentReading getContentReadingByContent(Content content);
//    ContentReadingResponse updatePutContentSpeaking(long id, ContentReadingRequest contentReadingRequest);
    ContentReading getContentReadingById(Long id);
    ContentReading handleCreateContentReading(ContentReadingRequest contentReadingRequest);
    void deleteContentReadingById(Long id);
    ContentReadingResponse updatePatchContentReading(long id, ContentReadingRequest request);
    Page<ContentReadingResponse> getContentReadingPage(int page, int size);
    ContentReadingVocabularyResponse addVocabularyToContentReading(Long contentReadingId, int vocabularyId );
    void removeVocabularyFromContentReading(Long contentReadingId, int vocabularyId);
    ContentReadingGrammarResponse addGrammarToContentReading(Long contentReadingId, int grammarId);
    void removeGrammarFromContentReading(Long contentReadingId, int grammarId);
}
