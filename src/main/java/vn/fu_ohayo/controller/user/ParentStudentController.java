package vn.fu_ohayo.controller.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.service.ParentStudentService;

@RestController()
@RequestMapping("/parent-student")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ParentStudentController {
   ParentStudentService parentStudentService;

    @GetMapping("/generateCode")
    public ApiResponse<String> generateCode () {
        return ApiResponse.<String>builder()
                .message("Sucess")
                .status("OK")
                .code("200")
                .data(parentStudentService.generateCode())
                .build();
    }
}
