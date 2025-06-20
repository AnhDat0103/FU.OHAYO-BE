package vn.fu_ohayo.controller.admin;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.ExerciseQuestionRequestForListening;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.ExerciseQuestionResponse;
import vn.fu_ohayo.entity.ExerciseQuestion;
import vn.fu_ohayo.service.ExerciseQuestionService;

import java.util.List;

@RestController
@RequestMapping("/question")
public class ContentListeningQuestionController {
private final ExerciseQuestionService exerciseQuestionService;

    public ContentListeningQuestionController(ExerciseQuestionService exerciseQuestionService) {
        this.exerciseQuestionService = exerciseQuestionService;
    }

    @GetMapping("/content_listening/{contentListeningId}")
    public ApiResponse<Page<ExerciseQuestionResponse>> getExerciseQuestionByContentListeningPage(
            @RequestParam(defaultValue="1" ) int page,
            @RequestParam(defaultValue = "5") int size,
            @PathVariable long contentListeningId) {
        return ApiResponse.<Page<ExerciseQuestionResponse>>builder()
                .code("200")
                .status("success")
                .message("get page of exercise questions by content listening id")
                .data(exerciseQuestionService.getExerciseQuestionPage(page - 1, size, contentListeningId))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ExerciseQuestionResponse> getExerciseQuestion(@PathVariable int id) {
        return  ApiResponse.<ExerciseQuestionResponse>builder()
                .code("200")
                .status("success")
                .message("Get exerciseQuestion by id")
                .data(exerciseQuestionService.getExerciseQuestionById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<ExerciseQuestionResponse> createExerciseQuestion(@Valid @RequestBody ExerciseQuestionRequestForListening request) {
        ExerciseQuestionResponse newExerciseQuestion =exerciseQuestionService.handleCreateExerciseQuestion(request);
        return ApiResponse.<ExerciseQuestionResponse>builder()
                .code("201")
                .status("success")
                .message("Created new ExerciseQuestion successfully")
                .data(newExerciseQuestion)
                .build();
    }

    @PostMapping("/many")
    public ApiResponse<List<ExerciseQuestionResponse>> createManyExerciseQuestion(@Valid @RequestBody List<ExerciseQuestionRequestForListening> requests) {
        List<ExerciseQuestionResponse> newExerciseQuestion =exerciseQuestionService.handleCreateAllExerciseQuestion(requests);
        return ApiResponse.<List<ExerciseQuestionResponse>>builder()
                .code("201")
                .status("success")
                .message("Created new ExerciseQuestion successfully")
                .data(newExerciseQuestion)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<ExerciseQuestion> deleteExerciseQuestion(@PathVariable int id) {
        exerciseQuestionService.deleteExerciseQuestionById(id);
        return ApiResponse.<ExerciseQuestion>builder()
                .code("200")
                .status("success")
                .message("Deleted content speaking successfully")
                .build();
    }

    @PatchMapping("/{id}")
    public  ApiResponse<ExerciseQuestionResponse> patchExerciseQuestion(
            @PathVariable int id,
            @Valid @RequestBody ExerciseQuestionRequestForListening request){
        ExerciseQuestionResponse ExerciseQuestionResponse = exerciseQuestionService.updatePatchExerciseQuestion(id,request );
        return ApiResponse.<ExerciseQuestionResponse>builder()
                .code("200")
                .status("success")
                .message("Updated all fields of content speaking")
                .data(ExerciseQuestionResponse)
                .build();
    }


}
