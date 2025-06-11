package vn.fu_ohayo.controller.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.fu_ohayo.config.PendingApprovalStorage;
import vn.fu_ohayo.dto.request.VerifyRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.service.ParentStudentService;

@RestController()
@RequestMapping("/parent-student")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ParentStudentController {
   ParentStudentService parentStudentService;
   PendingApprovalStorage storage;

    @GetMapping("/generateCode")
    public ApiResponse<String> generateCode () {
        return ApiResponse.<String>builder()
                .message("Sucess")
                .status("OK")
                .code("200")
                .data(parentStudentService.generateCode())
                .build();
    }

    @GetMapping("extract-code")
     public ApiResponse<String> extractCode (@RequestParam String code) {
        return ApiResponse.<String>builder()
                .message("Sucess")
                .status("OK")
                .code("200")
                .data(parentStudentService.generateCode())
                .build();
    }

    @MessageMapping("/request-verify")
    public void handleVerifyRequest(VerifyRequest request) {
        storage.save(request.getClientId(), request.getParentCode());
        // Không gửi phản hồi vội
    }
}
