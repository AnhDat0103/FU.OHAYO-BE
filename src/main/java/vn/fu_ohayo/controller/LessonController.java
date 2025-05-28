package vn.fu_ohayo.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.LessonPatchRequest;
import vn.fu_ohayo.dto.request.LessonRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.LessonResponse;
import vn.fu_ohayo.service.LessonService;


@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/{subjectId}")
    public ApiResponse<Page<LessonResponse>> getAllLessons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @PathVariable int subjectId
        ) {
        Page<LessonResponse> lessons = lessonService.getAllLessons(subjectId,page, size);
        return ApiResponse.<Page<LessonResponse>>builder()
                .status("success")
                .message("Fetched all lessons successfully")
                .data(lessons)
                .build();
    }

    @PostMapping("")
    public ApiResponse<LessonResponse> createLesson(@Valid @RequestBody LessonRequest lessonRequest) {
        LessonResponse createdLesson = lessonService.createLesson(lessonRequest);
        return ApiResponse.<LessonResponse>builder()
                .status("success")
                .message("Lesson created successfully")
                .data(createdLesson)
                .build();
    }

    @PatchMapping("/{id}")
    public ApiResponse<LessonResponse> updateLesson(@PathVariable Integer id, @RequestBody LessonPatchRequest lessonRequest) {
        LessonResponse updatedLesson = lessonService.updateLesson(id, lessonRequest);
        return ApiResponse.<LessonResponse>builder()
                .status("success")
                .message("Lesson updated successfully")
                .data(updatedLesson)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteLesson(@PathVariable Integer id) {
        lessonService.deleteLesson(id);
        return ApiResponse.<Void>builder()
                .status("success")
                .message("Lesson deleted successfully")
                .build();
    }
}
