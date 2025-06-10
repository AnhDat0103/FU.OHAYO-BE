package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.response.ProgressVocabularyResponse;
import vn.fu_ohayo.entity.Vocabulary;

import java.util.List;

public interface ProgressVocabularyService {
    int countVocabularyLearnSubjectInProgressByUserId(long userId);
    int countAllVocabularySubjectInProgressByUserId(long userId);

}