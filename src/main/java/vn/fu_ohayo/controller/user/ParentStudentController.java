package vn.fu_ohayo.controller.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.CodeRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.ApprovalResponse;
import vn.fu_ohayo.dto.response.LearningProgressOverviewResponse;
import vn.fu_ohayo.dto.response.UserResponse;
import vn.fu_ohayo.mapper.UserMapper;
import vn.fu_ohayo.service.*;

@RestController()
@RequestMapping("/parent-student")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j(topic = "ParenController")
public class ParentStudentController {
   ParentStudentService parentStudentService;
    ExerciseResultService exerciseResultService;
    ProgressGrammarService progressGrammarService;
    ProgressVocabularyService progressVocabularyService;
    UserService userService;
    UserMapper userMapper;
//    @Autowired
//    SimpMessagingTemplate messa;

    @GetMapping("/generateCode")
    public ApiResponse<String> generateCode () {
        String code = parentStudentService.generateCode();
        if("".equals(code)) {
            return ApiResponse.<String>builder()
                    .message("Failed")
                    .status("Failed")
                    .code("401")
                    .data("")
                    .build();
        }
        return ApiResponse.<String>builder()
                .message("Sucess")
                .status("OK")
                .code("200")
                .data(code)
                .build();
    }


//    @MessageMapping("/parent/approve")
//    public void handleVerifyRequest(VerifyRequest request) {
//        String mess = parentStudentService.extractCode(request.getParentCode(), request.getClientId());
//        if(!"".equals(mess)) {
//            messa.convertAndSend("/topic/approval",
//                    new ApprovalResponse(request.getClientId(), false, mess));
//        }
//        messa.convertAndSend("/topic/approval",
//                new ApprovalResponse(request.getClientId(), false, "Your code have send. Wait for verify of parent!"));
//    }

    @PostMapping("/approve")
    public ApiResponse<?> handleVerifyRequest(@RequestBody CodeRequest request) {
        String mess = parentStudentService.extractCode(request.getParentCode());
        if(!"".equals(mess)) {
           return ApiResponse.builder()
                   .status("Oke")
                   .message("Thanh cong")
                   .data(new ApprovalResponse(false, mess))
                   .build();
        }
        return ApiResponse.builder()
                .status("Oke")
                .message("Thanh cong")
                .data(new ApprovalResponse(false, "Your code have send. Wait for verify of parent!"))
                .build();
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
    public ApiResponse<?> getInformationByUserId(@RequestParam long userId
    ) {
             UserResponse userResponse = userMapper.toUserResponse(userService.getUserById(userId));
        return ApiResponse.<UserResponse>builder()
                .status("success")
                .message("Fetched user information successfully")
                .data(userResponse)
                .build();
    }

}

