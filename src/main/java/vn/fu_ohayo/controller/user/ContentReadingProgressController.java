package vn.fu_ohayo.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.service.ContentReadingProgressService;

import java.util.List;

@RestController
@RequestMapping("/progressReading")
@RequiredArgsConstructor
@Slf4j(topic = "ContentReadingProgressController")
public class ContentReadingProgressController {
    private final ContentReadingProgressService contentReadingProgressService;

    @PostMapping("/markAsDone")
    public ApiResponse<String> markAsDone(
            @RequestParam Long userId,
            @RequestParam Long contentReadingId
            ){
        log.info("Marking reading progress for userId: {}, contentReadingId: {}", userId, contentReadingId);
        return contentReadingProgressService.markReadingProgress(userId, contentReadingId);
    }
    @GetMapping("/checkStatus")
    //checkstatus kiem tra xem nguoi dung da hoan thanh doc bai hay chua
    public ApiResponse<Boolean> checkStatus(
            @RequestParam Long userId,
            @RequestParam Long contentReadingId
    ){
        log.info(contentReadingId.toString());
        boolean isDone = contentReadingProgressService.isDoneReading(userId, contentReadingId);
        return ApiResponse.<Boolean>builder()
                .code("200")
                .status("success")
                .message("Check reading progress status")
                .data(isDone)
                .build();
    }

}