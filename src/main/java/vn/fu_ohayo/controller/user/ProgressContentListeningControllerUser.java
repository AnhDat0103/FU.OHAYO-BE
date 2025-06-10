package vn.fu_ohayo.controller.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.entity.ContentListening;
import vn.fu_ohayo.entity.ProgressContent;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.repository.ContentListeningRepository;
import vn.fu_ohayo.repository.ProgressContentRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.ProgressContentService;
import java.util.List;

@RestController
@RequestMapping("/api/user/contents_listening")
@AllArgsConstructor
public class ProgressContentListeningControllerUser {

    private ProgressContentService progressContentService;
    private UserRepository userRepository;
    private ProgressContentRepository progressContentRepository;
    private ContentListeningRepository contentListeningRepository;

    @PostMapping("/progress")
    public ProgressContent saveProgress(
            @RequestParam Long userId,
            @RequestParam Long contentListeningId,
            @RequestParam int progress) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        ContentListening contentListening = contentListeningRepository.findById(contentListeningId).orElseThrow(()
                -> new RuntimeException("ContentListening not found with id: " + contentListeningId));


        return progressContentService.saveUserProgress(user, contentListening, progress);
    }
}