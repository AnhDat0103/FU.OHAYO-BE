package vn.fu_ohayo.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.fu_ohayo.dto.request.ExerciseQuestionRequest;
import vn.fu_ohayo.dto.response.ProgressContentResponse;
import vn.fu_ohayo.entity.ProgressContent;
import vn.fu_ohayo.mapper.ProgressContentMapper;
import vn.fu_ohayo.service.ContentListeningProgressService;

import java.util.List;

@RestController
@RequestMapping("/api/user/content-listening")
@RequiredArgsConstructor
public class ContentUserListeningController {
    private  final ProgressContentMapper progressContentMapper;
    private final ContentListeningProgressService contentListeningProgressService;

    @PostMapping("/submit-answers")
    public ResponseEntity<ProgressContentResponse> submitListeningAnswers(
            @RequestParam Long userId,
            @RequestParam Long contentListeningId,
            @RequestBody List<ExerciseQuestionRequest> userAnswers) {

        ProgressContent progressContent = contentListeningProgressService.saveListeningProgress(
                userId, contentListeningId, userAnswers);

        return ResponseEntity.ok(
                progressContentMapper.toResponse(progressContent)
        );
    }
}