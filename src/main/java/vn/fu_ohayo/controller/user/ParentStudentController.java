package vn.fu_ohayo.controller.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.CodeRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.ApprovalResponse;
import vn.fu_ohayo.service.ParentStudentService;

@RestController()
@RequestMapping("/parent-student")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j(topic = "ParenController")
public class ParentStudentController {
   ParentStudentService parentStudentService;
//    @Autowired
//    SimpMessagingTemplate messa;

    @GetMapping("/generateCode")
    public ResponseEntity<ApiResponse<String>> generateCode () {
        String code = parentStudentService.generateCode();
        return ResponseEntity.ok().body(ApiResponse.<String>builder()
                .message("Sucess")
                .status("OK")
                .code("200")
                .data(code)
                .build());
    }



    @PostMapping("/approve")
    public ApiResponse<?> handleVerifyRequest(@RequestBody CodeRequest request) {
        String mess = parentStudentService.extractCode(request.getParentCode());
        if(!"".equals(mess)) {
           return ApiResponse.builder()
                   .status("Oke")
                   .message("Thanh cong")
                   .data(new ApprovalResponse(false, mess))
                   .build();
        }
        return ApiResponse.builder()
                .status("Oke")
                .message("Thanh cong")
                .data(new ApprovalResponse(false, "Your code have send. Wait for verify of parent!"))
                .build();
    }


}

