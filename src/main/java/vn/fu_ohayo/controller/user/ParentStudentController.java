package vn.fu_ohayo.controller.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.VerifyRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.ApprovalResponse;
import vn.fu_ohayo.service.ParentStudentService;

@RestController()
@RequestMapping("/parent-student")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ParentStudentController {
   ParentStudentService parentStudentService;
    @Autowired
    SimpMessagingTemplate messa;

    @GetMapping("/generateCode")
    public ApiResponse<String> generateCode () {
        return ApiResponse.<String>builder()
                .message("Sucess")
                .status("OK")
                .code("200")
                .data(parentStudentService.generateCode())
                .build();
    }


    @MessageMapping("/parent/approve")
    public void handleVerifyRequest(VerifyRequest request) {
        String mess = parentStudentService.extractCode(request.getParentCode(), request.getClientId());
        if(!"".equals(mess)) {
            messa.convertAndSend("/topic/approval",
                    new ApprovalResponse(request.getClientId(), false, mess));
        }
        messa.convertAndSend("/topic/approval",
                new ApprovalResponse(request.getClientId(), false, "Your code have send. Wait for verify of parent!"));
    }



    @MessageMapping("/hello") // client gửi tới /app/hello
    public void handleHello(String message) {
        System.out.println("Nhận được từ client: " + message);
        messa.convertAndSend("/topic/greetings", "Hello từ server! Nội dung: " + message);
    }
}

