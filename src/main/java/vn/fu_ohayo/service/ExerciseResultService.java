package vn.fu_ohayo.service;

public interface ExerciseResultService {
    int countExerciseDoneSubjectInProgressByUserId(long userId);
    int countAllExerciseSubjectInProgressByUserId(long userId);
}
