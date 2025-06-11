package vn.fu_ohayo.controller.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.fu_ohayo.config.PendingApprovalStorage;
import vn.fu_ohayo.dto.request.VerifyRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.LearningProgressOverviewResponse;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.mapper.UserMapper;
import vn.fu_ohayo.service.*;

@RestController()
@RequestMapping("/parent-student")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ParentStudentController {
   ParentStudentService parentStudentService;
   PendingApprovalStorage storage;
    ExerciseResultService exerciseResultService;
    ProgressGrammarService progressGrammarService;
    ProgressVocabularyService progressVocabularyService;
    UserService userService;
    UserMapper userMapper;

    @GetMapping("/generateCode")
    public ApiResponse<String> generateCode () {
        return ApiResponse.<String>builder()
                .message("Sucess")
                .status("OK")
                .code("200")
                .data(parentStudentService.generateCode())
                .build();
    }

    @GetMapping("extract-code")
     public ApiResponse<String> extractCode (@RequestParam String code) {
        return ApiResponse.<String>builder()
                .message("Sucess")
                .status("OK")
                .code("200")
                .data(parentStudentService.generateCode())
                .build();
    }

    @MessageMapping("/request-verify")
    public void handleVerifyRequest(VerifyRequest request) {
        storage.save(request.getClientId(), request.getParentCode());
        // Không gửi phản hồi vội
    }

    @GetMapping("/student/overview")
    ApiResponse<LearningProgressOverviewResponse> getLearningProgressResponse(
            @RequestParam long userId
    ) {
        LearningProgressOverviewResponse response = LearningProgressOverviewResponse.builder()
                .totalVocabularyAllSubject(progressVocabularyService.countAllVocabularySubjectInProgressByUserId(userId))
                .totalVocabularyLearn(progressVocabularyService.countVocabularyLearnSubjectInProgressByUserId(userId))
                .exerciseAllSubject(exerciseResultService.countAllExerciseSubjectInProgressByUserId(userId))
                .exerciseCompleted(exerciseResultService.countExerciseDoneSubjectInProgressByUserId(userId))
                .totalGrammarAllSubject(progressVocabularyService.countAllVocabularySubjectInProgressByUserId(userId))
                .totalGrammarLearn(progressGrammarService.countGrammarLearnSubjectInProgressByUserId(userId))
                .build();
        return ApiResponse.<LearningProgressOverviewResponse>builder()
                .status("success")
                .message("Fetched all lessons successfully")
                .data(response)
                .build();
    }

    @GetMapping("/student/vocabulary")
    ApiResponse<?> getProgressVocabularyByUserId(
            @RequestParam long userId
    ) {
        return ApiResponse.<Object>builder()
                .status("success")
                .message("Fetched vocabulary progress successfully")
                .data(progressVocabularyService.getProgressEachSubjectByUserId(userId))
                .build();
    }

    @GetMapping("/student/grammar")
    ApiResponse<?> getProgressGrammarByUserId(
            @RequestParam long userId

    ) {
        return ApiResponse.<Object>builder()
                .status("success")
                .message("Fetched grammar progress successfully")
                .data(progressGrammarService.getProgressEachSubjectByUserId(userId))
                .build();
    }

    @GetMapping("/student/exercise")
    ApiResponse<?> getProgressExerciseByUserId(
            @RequestParam long userId
    ) {
        return ApiResponse.<Object>builder()
                .status("success")
                .message("Fetched exercise progress successfully")
                .data(exerciseResultService.getProgressEachSubjectByUserId(userId))
                .build();
    }

    @GetMapping("/student/information")
    ApiResponse<?> getStudentInformation(
            @RequestParam long userId
    ) {
        return ApiResponse.<Object>builder()
                .status("success")
                .message("Fetched student information successfully")
                .data(userMapper.toUserResponse(userService.getUserById(userId)))
                .build();
    }
}
