package vn.fu_ohayo.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.ProgressSubjectResponse;
import vn.fu_ohayo.service.ProgressSubjectService;

@RestController
@RequestMapping("/progress-subjects")
public class ProgressSubjectController {

    private final ProgressSubjectService progressSubjectService;

    public ProgressSubjectController(ProgressSubjectService progressSubjectService) {
        this.progressSubjectService = progressSubjectService;
    }

    @GetMapping()
    public ApiResponse<ProgressSubjectResponse> getProgressSubject(
            @RequestParam int subjectId,
            @RequestParam long userId
    ){
        return ApiResponse.<ProgressSubjectResponse>builder()
                .code("200")
                .status("success")
                .message("Get progress subject successfully")
                .data(progressSubjectService.getProgressSubject(userId, subjectId))
                .build();

    }
}
