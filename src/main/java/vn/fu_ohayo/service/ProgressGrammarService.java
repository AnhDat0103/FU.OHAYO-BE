package vn.fu_ohayo.service;

public interface ProgressGrammarService {
    int countGrammarLearnSubjectInProgressByUserId(long userId);
    int countAllGrammarSubjectInProgressByUserId(long userId);
}
