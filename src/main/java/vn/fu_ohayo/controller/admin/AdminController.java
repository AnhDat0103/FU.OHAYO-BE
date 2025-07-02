package vn.fu_ohayo.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import vn.fu_ohayo.dto.response.AdminResponse;
import vn.fu_ohayo.dto.response.AdminSearchUserResponse;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.service.AdminService;

import static vn.fu_ohayo.constant.ConstantGolbal.HTTP_SUCCESS_CODE_RESPONSE;
import static vn.fu_ohayo.constant.ConstantGolbal.HTTP_SUCCESS_RESPONSE;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public ApiResponse<Page<AdminResponse>> filterUsers(@RequestParam(required = false) String searchName,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<Page<AdminResponse>>builder()
                .code(HTTP_SUCCESS_CODE_RESPONSE)
                .status(HTTP_SUCCESS_RESPONSE)
                .message("Get admin by name successfully")
                .data(adminService.getAllByName(searchName, page, size))
                .build();
    }


}
