package vn.fu_ohayo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.entity.ContentListening;
import vn.fu_ohayo.service.ContentListeningService;

import java.util.List;

@RestController
@RequestMapping("/api/user/contents_listening")
@AllArgsConstructor
public class ContentListeningControllerUser {

    private final ContentListeningService contentListeningService;

    @PostMapping("/{user-id}")
    public ResponseEntity<?> getContentListeningByUserId(@PathVariable("user-id") Long userId) {
        List<ContentListening> contentListenings = contentListeningService.getContentListeningByUserId(userId);
        if (contentListenings.isEmpty()) {
            return ResponseEntity.status(404).body("No content listening found for this user");
        }
        return ResponseEntity.ok(contentListenings);
    }
}