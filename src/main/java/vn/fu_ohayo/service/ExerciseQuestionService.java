package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.request.ExerciseQuestionRequestForListening;
import vn.fu_ohayo.dto.response.AnswerQuestionResponse;
import vn.fu_ohayo.dto.response.ExerciseQuestionResponse;
import vn.fu_ohayo.entity.ExerciseQuestion;
import vn.fu_ohayo.repository.AnswerQuestionRepository;
import vn.fu_ohayo.repository.ExerciseQuestionRepository;

import java.util.List;
import java.util.Set;

public interface ExerciseQuestionService {


    Page<ExerciseQuestionResponse> getExerciseQuestionPage(int page,int size, long contentListeningId);
    ExerciseQuestionResponse getExerciseQuestionById(int id);
    ExerciseQuestionResponse handleCreateExerciseQuestion(ExerciseQuestionRequestForListening ExerciseQuestionRequestForListening);
    void deleteExerciseQuestionById(int id);
    ExerciseQuestionResponse updatePatchExerciseQuestion(int id, ExerciseQuestionRequestForListening ExerciseQuestionRequestForListening);
    List<ExerciseQuestionResponse> handleCreateAllExerciseQuestion(
            List<ExerciseQuestionRequestForListening> ExerciseQuestionRequestForListenings);
}
