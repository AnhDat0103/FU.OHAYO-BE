package vn.fu_ohayo.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.SubjectRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.SubjectResponse;
import vn.fu_ohayo.enums.LessonStatus;
import vn.fu_ohayo.enums.SubjectStatus;
import vn.fu_ohayo.service.SubjectService;

import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController {
    
    private final SubjectService subjectService;
    
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ApiResponse<List<SubjectResponse>> getAllSubjects() {
        
        return ApiResponse.<List<SubjectResponse>>builder()
                .code("200")
                .status("success")
                .message("Get all subjects successfully")
                .data(subjectService.getAllSubjects())
                .build();
    }

    @PostMapping
    public ApiResponse<SubjectResponse> createSubject(@Valid @RequestBody SubjectRequest subjectRequest) {
        return ApiResponse.<SubjectResponse>builder()
                .code("201")
                .status("success")
                .message("Subject created successfully")
                .data(subjectService.createSubject(subjectRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<SubjectResponse> updateSubject(@PathVariable("id") int id,
                                                      @Valid @RequestBody SubjectRequest subjectRequest) {
        return ApiResponse.<SubjectResponse>builder()
                .code("200")
                .status("success")
                .message("Subject updated successfully")
                .data(subjectService.updateSubject(id, subjectRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSubject(@PathVariable("id") int id) {
        subjectService.deleteSubject(id);
        return ApiResponse.<Void>builder()
                .code("200")
                .status("success")
                .message("Subject deleted successfully")
                .build();
    }

    @GetMapping("/status")
    public ApiResponse<List<SubjectStatus>> getLessonStatuses() {
        List<SubjectStatus> statuses = List.of(SubjectStatus.values());
        return ApiResponse.<List<SubjectStatus>>builder()
                .code("200")
                .status("success")
                .message("Fetched lesson statuses successfully")
                .data(statuses)
                .build();
    }
}
