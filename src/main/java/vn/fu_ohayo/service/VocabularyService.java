package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.response.VocabularyResponse;

import java.util.List;

public interface VocabularyService {

    List<VocabularyResponse> getAllVocabularies(int lessonId);

    VocabularyResponse getVocabularyById(String id);

    VocabularyResponse handleSaveVocabulary(int lessonId, VocabularyResponse vocabularyResponse);

    VocabularyResponse updatePutVocabulary(int lessonId, VocabularyResponse vocabularyResponse);

    VocabularyResponse updatePatchVocabulary(String id, VocabularyResponse vocabularyResponse);

    void deleteVocabularyById(String id);

    Page<VocabularyResponse> getVocabularyPage(int page, int size);
}
