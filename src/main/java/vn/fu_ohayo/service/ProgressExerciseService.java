package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.request.UserResponseRequest;
import vn.fu_ohayo.dto.response.ExerciseResultResponse;

public interface ProgressExerciseService {

    ExerciseResultResponse submitExercise(UserResponseRequest userResponseRequest);
}
