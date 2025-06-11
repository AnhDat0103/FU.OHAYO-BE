package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.response.ProgressGrammarResponse;

public interface ProgressGrammarService {
    int countGrammarLearnSubjectInProgressByUserId(long userId);
    int countAllGrammarSubjectInProgressByUserId(long userId);
    ProgressGrammarResponse getProgressEachSubjectByUserId(long userId);

}
