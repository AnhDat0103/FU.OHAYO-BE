package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.response.ProgressVocabularyResponse;

public interface ProgressVocabularyService {
    int countVocabularyLearnSubjectInProgressByUserId(long userId);
    int countAllVocabularySubjectInProgressByUserId(long userId);
    ProgressVocabularyResponse getProgressEachSubjectByUserId(long userId);

}