package vn.fu_ohayo.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import vn.fu_ohayo.dto.request.AnswerListeningRequest;
import vn.fu_ohayo.dto.request.ExerciseQuestionRequest;
import vn.fu_ohayo.dto.response.ProgressContentResponse;
import vn.fu_ohayo.entity.ProgressContent;
import vn.fu_ohayo.mapper.ProgressContentMapper;
import vn.fu_ohayo.service.ContentListeningProgressService;
import vn.fu_ohayo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("content-listening")
@RequiredArgsConstructor
public class ContentUserListeningController {
    private  final ProgressContentMapper progressContentMapper;
    private final ContentListeningProgressService contentListeningProgressService;
    private final UserService userService;

    @PostMapping("/submit-answers")
    public ResponseEntity<ProgressContentResponse> submitListeningAnswers(
            @RequestParam Long contentListeningId,
            @RequestBody List<AnswerListeningRequest> userAnswers) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) auth.getPrincipal()).getUsername();
        long userId = userService.getUserByEmail(email).getUserId();
        ProgressContent progressContent = contentListeningProgressService.saveListeningProgress(
                userId, contentListeningId, userAnswers );

        return ResponseEntity.ok(
                progressContentMapper.toResponse(progressContent)
        );
    }
}