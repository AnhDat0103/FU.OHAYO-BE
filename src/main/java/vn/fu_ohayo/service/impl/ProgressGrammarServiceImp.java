package vn.fu_ohayo.service.impl;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.ProgressStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.repository.ProgressGrammarRepository;
import vn.fu_ohayo.repository.ProgressSubjectRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.ProgressGrammarService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgressGrammarServiceImp implements ProgressGrammarService {
    private final ProgressGrammarRepository progressGrammarRepository;
    private final UserRepository userRepository;
    private final ProgressSubjectRepository progressSubjectRepository;

    public ProgressGrammarServiceImp(ProgressGrammarRepository progressGrammarRepository, UserRepository userRepository, ProgressSubjectRepository progressSubjectRepository) {
        this.progressGrammarRepository = progressGrammarRepository;
        this.userRepository = userRepository;
        this.progressSubjectRepository = progressSubjectRepository;
    }

    private List<Grammar> getGrammarsOfSubjectsInProgress(User user) {
        List<ProgressSubject> progressSubjects = progressSubjectRepository
                .findAllByUserAndProgressStatus(user, ProgressStatus.IN_PROGRESS);
        return progressSubjects.stream()
                .filter(e -> e.getProgressStatus().equals(ProgressStatus.IN_PROGRESS))
                .map(ProgressSubject::getSubject)
                .map(Subject::getLessons)
                .flatMap(List::stream)
                .map(Lesson::getGrammars)
                .flatMap(List::stream)
                .toList();
    }

    @Override
    public int countGrammarLearnSubjectInProgressByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow( () -> new AppException(ErrorEnum.USER_NOT_FOUND));
        List<Grammar> grammars = getGrammarsOfSubjectsInProgress(user);

        List<ProgressGrammar> progressGrammars = progressGrammarRepository
                .findAllByUserAndProgressStatusAndGrammarIn(user, ProgressStatus.COMPLETED, grammars);
        return progressGrammars.size();
    }

    @Override
    public int countAllGrammarSubjectInProgressByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow( () -> new AppException(ErrorEnum.USER_NOT_FOUND));
        List<Grammar> grammars = getGrammarsOfSubjectsInProgress(user);

        List<ProgressGrammar> progressGrammars = progressGrammarRepository
                .findAllByUserAndGrammarIn(user, grammars);
        return progressGrammars.size();
    }
}
