package vn.fu_ohayo.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.LearningProgressOverviewResponse;
import vn.fu_ohayo.service.ExerciseResultService;
import vn.fu_ohayo.service.GrammarService;
import vn.fu_ohayo.service.ProgressGrammarService;
import vn.fu_ohayo.service.ProgressVocabularyService;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ExerciseResultService exerciseResultService;
    private final ProgressGrammarService progressGrammarService;
    private final ProgressVocabularyService progressVocabularyService;
    private final GrammarService grammarService;

    public ProfileController(ExerciseResultService exerciseResultService, ProgressGrammarService progressGrammarService, ProgressVocabularyService progressVocabularyService, GrammarService grammarService) {
        this.exerciseResultService = exerciseResultService;
        this.progressGrammarService = progressGrammarService;
        this.progressVocabularyService = progressVocabularyService;
        this.grammarService = grammarService;
    }

    @GetMapping("/overview/learning_progress")
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
}
