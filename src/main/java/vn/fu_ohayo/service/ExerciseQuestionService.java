package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.request.ExerciseQuestionRequest;
import vn.fu_ohayo.dto.response.AnswerQuestionResponse;
import vn.fu_ohayo.dto.response.ExerciseQuestionResponse;
import vn.fu_ohayo.entity.ExerciseQuestion;
import vn.fu_ohayo.repository.AnswerQuestionRepository;
import vn.fu_ohayo.repository.ExerciseQuestionRepository;

import java.util.List;
import java.util.Set;

public interface ExerciseQuestionService {


    Page<ExerciseQuestionResponse> getExerciseQuestionByContentListeingPage(int page,int size, long contentListeningId);
    ExerciseQuestionResponse getExerciseQuestionById(int id);
    ExerciseQuestionResponse handleCreateExerciseQuestion(ExerciseQuestionRequest ExerciseQuestionRequest);
    void deleteExerciseQuestionById(int id);
    ExerciseQuestionResponse updatePatchExerciseQuestion(int id, ExerciseQuestionRequest ExerciseQuestionRequest);
    List<ExerciseQuestionResponse> handleCreateAllExerciseQuestion(
    List<ExerciseQuestionRequest> ExerciseQuestionRequests);
    Page<ExerciseQuestionResponse> getExerciseQuestionPage(int page,int size);
    ExerciseQuestionResponse acceptExerciseQuestion (int id);
    ExerciseQuestionResponse rejectExerciseQuestion (int id);

}
