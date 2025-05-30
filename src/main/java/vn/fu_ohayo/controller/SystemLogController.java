package vn.fu_ohayo.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.fu_ohayo.dto.request.SystemLogRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.SystemLogResponse;
import vn.fu_ohayo.service.SystemLogService;

import java.util.List;

@RestController()
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin/system-logs")
public class SystemLogController {

    SystemLogService systemLogService;

    @GetMapping
    public ApiResponse<List<SystemLogResponse>> getSystemLog() {
        return ApiResponse.<List<SystemLogResponse>>builder()
                .code("200")
                .status("success")
                .message("Get all system log successfully")
                .data(systemLogService.getAllLogs())
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<SystemLogResponse>> searchSystemLog(@Valid @ModelAttribute SystemLogRequest request) {
        return ApiResponse.<List<SystemLogResponse>>builder()
                .code("200")
                .status("success")
                .message("Get system by timestamp, action, user successfully")
                .data(systemLogService.searchSystemLog(request))
                .build();
    }
}
