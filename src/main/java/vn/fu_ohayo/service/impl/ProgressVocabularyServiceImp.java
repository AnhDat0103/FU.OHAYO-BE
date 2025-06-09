package vn.fu_ohayo.service.impl;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.ProgressStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.repository.ProgressSubjectRepository;
import vn.fu_ohayo.repository.ProgressVocabularyRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.ProgressVocabularyService;
import java.util.List;

@Service
public class ProgressVocabularyServiceImp implements ProgressVocabularyService {

    private  final ProgressVocabularyRepository progressVocabularyRepository;
    private  final UserRepository userRepository;
    private final ProgressSubjectRepository progressSubjectRepository;

    public ProgressVocabularyServiceImp(ProgressVocabularyRepository progressVocabularyRepository, UserRepository userRepository, ProgressSubjectRepository progressSubjectRepository) {
        this.progressVocabularyRepository = progressVocabularyRepository;
        this.userRepository = userRepository;
        this.progressSubjectRepository = progressSubjectRepository;
    }
    private List<Vocabulary> getVocabulariesOfSubjectsInProgress(User user) {
        List<ProgressSubject> progressSubjects = progressSubjectRepository
                .findAllByUserAndProgressStatus(user, ProgressStatus.IN_PROGRESS);
        return progressSubjects.stream()
                .filter(e -> e.getProgressStatus().equals(ProgressStatus.IN_PROGRESS))
                .map(ProgressSubject::getSubject)
                .map(Subject::getLessons)
                .flatMap(List::stream)
                .map(Lesson::getVocabularies)
                .flatMap(List::stream)
                .toList();
    }

    @Override
    public int countVocabularyLearnSubjectInProgressByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow( () -> new AppException(ErrorEnum.USER_NOT_FOUND));
        List<Vocabulary> vocabularies = getVocabulariesOfSubjectsInProgress(user);

        List<ProgressVocabulary> progressVocabularies = progressVocabularyRepository
                .findAllByUserAndProgressStatusAndVocabularyIn(user, ProgressStatus.COMPLETED, vocabularies);
        return progressVocabularies.size();
    }

    @Override
    public int countAllVocabularySubjectInProgressByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow( () -> new AppException(ErrorEnum.USER_NOT_FOUND));
        List<Vocabulary> vocabularies = getVocabulariesOfSubjectsInProgress(user);

        List<ProgressVocabulary> progressVocabularies = progressVocabularyRepository
                .findAllByUserAndVocabularyIn(user, vocabularies);
        return progressVocabularies.size();
    }

}
