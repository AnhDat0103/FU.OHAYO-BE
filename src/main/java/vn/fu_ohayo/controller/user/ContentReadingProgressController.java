package vn.fu_ohayo.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.service.ContentReadingProgressService;

import java.util.List;

@RestController
@RequestMapping("/api/progressReading")
@RequiredArgsConstructor
public class ContentReadingProgressController {
    private final ContentReadingProgressService contentReadingProgressService;

    @PostMapping("/markAsDone")
    public ApiResponse<String> markAsDone(
            @RequestParam Long userId,
            @RequestParam Long contentReadingId
            ){
        return contentReadingProgressService.markReadingProgress(userId, contentReadingId);
    }
    @GetMapping("/checkStatus")
    //checkstatus kiem tra xem nguoi dung da hoan thanh doc bai hay chua
    public ApiResponse<Boolean> checkStatus(
            @RequestParam Long userId,
            @RequestParam Long contentReadingId
    ){
        boolean isDone = contentReadingProgressService.isDoneReading(userId, contentReadingId);
        return ApiResponse.<Boolean>builder()
                .code("200")
                .status("success")
                .message("Check reading progress status")
                .data(isDone)
                .build();
    }

}