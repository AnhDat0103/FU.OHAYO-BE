package vn.fu_ohayo.controller.admin;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.fu_ohayo.dto.request.SubjectRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.SubjectResponse;
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
    public ApiResponse<Page<SubjectResponse>> getAllSubjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        
        return ApiResponse.<Page<SubjectResponse>>builder()
                .code("200")
                .status("success")
                .message("Get all subjects successfully")
                .data(subjectService.getAllSubjectsForAdmin(page, size))
                .build();
    }

    @PostMapping
    public ApiResponse<SubjectResponse> createSubject(@Valid @RequestBody SubjectRequest subjectRequest, MultipartFile image, MultipartFile video) {
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

    @GetMapping("/{id}")   //@GetMapping("/{id:\\d+}")
    public ApiResponse<SubjectResponse> getSubjectById(@PathVariable("id") int id) {
        return ApiResponse.<SubjectResponse>builder()
                .code("200")
                .status("success")
                .message("Fetched subject successfully")
                .data(subjectService.getSubjectById(id))
                .build();
    }

    @GetMapping("/students")
    public  ApiResponse<Page<SubjectResponse>> getAllByUserId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam() long userId
            ){
        Page<SubjectResponse> subjectResponses = subjectService.getAllByUserId(page, size, userId);
        return ApiResponse.<Page<SubjectResponse>>builder()
                .code("200")
                .status("success")
                .message("Fetched subjects by user ID successfully")
                .data(subjectResponses)
                .build();
    }
}
