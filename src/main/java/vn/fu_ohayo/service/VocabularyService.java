package vn.fu_ohayo.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.request.VocabularyRequest;
import vn.fu_ohayo.dto.response.GrammarResponse;
import vn.fu_ohayo.dto.response.VocabularyResponse;

import java.util.List;

public interface VocabularyService {

    List<VocabularyResponse> getAllVocabularies(int lessonId);

    VocabularyResponse handleSaveVocabulary(int lessonId, VocabularyRequest vocabularyRequest);

    VocabularyResponse updatePutVocabulary(int vocabularyId, VocabularyRequest vocabularyRequest);
    void deleteVocabularyById(int vocabularyId);

    Page<VocabularyResponse> getVocabularyPage(int page, int size, int lessonId);
    Page<VocabularyResponse> getAllVocabulariesPage(int page, int size);

    VocabularyResponse handleSaveVocabulary(@Valid VocabularyRequest vocabularyRequest);
}
