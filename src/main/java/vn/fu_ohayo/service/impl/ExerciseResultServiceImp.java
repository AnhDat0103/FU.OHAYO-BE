package vn.fu_ohayo.service.impl;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.ProgressStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.repository.ExerciseResultRepository;
import vn.fu_ohayo.repository.ProgressSubjectRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.ExerciseResultService;

import java.util.List;

@Service
public class ExerciseResultServiceImp implements ExerciseResultService {

    private final ExerciseResultRepository exerciseResultRepository;
    private final UserRepository userRepository;
    private final ProgressSubjectRepository progressSubjectRepository;

    public ExerciseResultServiceImp(ExerciseResultRepository exerciseResultRepository, UserRepository userRepository, ProgressSubjectRepository progressSubjectRepository) {
        this.exerciseResultRepository = exerciseResultRepository;
        this.userRepository = userRepository;
        this.progressSubjectRepository = progressSubjectRepository;
    }

    private List<LessonExercise> getLessonExercisesOfSubjectsInProgress(User user) {
        List<ProgressSubject> progressSubjects = progressSubjectRepository
                .findAllByUserAndProgressStatus(user, ProgressStatus.IN_PROGRESS);
        return progressSubjects.stream()
                .filter(e -> e.getProgressStatus().equals(ProgressStatus.IN_PROGRESS))
                .map(ProgressSubject::getSubject)
                .map(Subject::getLessons)
                .flatMap(List::stream)
                .map(Lesson::getLessonExercises)
                .flatMap(List::stream)
                .toList();
    }

    @Override
    public int countExerciseDoneSubjectInProgressByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrorEnum.USER_NOT_FOUND));
        List<LessonExercise> lessonExercises = getLessonExercisesOfSubjectsInProgress(user);
        return exerciseResultRepository.findAllByUserAndLessonExerciseIn(user, lessonExercises).size();
    }

    @Override
    public int countAllExerciseSubjectInProgressByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrorEnum.USER_NOT_FOUND));
        return getLessonExercisesOfSubjectsInProgress(user).size();
    }
}
