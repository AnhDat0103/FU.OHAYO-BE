package vn.fu_ohayo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.service.UploadService;

@RestController
@RequestMapping("/uploadfile")
public class UploadFileController {
    private final UploadService uploadService;

    public UploadFileController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping
    public ApiResponse<String> uploadFile(@RequestParam("file") MultipartFile file,@RequestParam("targetFolder") String targetFolder) {
        String url = uploadService.handleUploadFile(file, targetFolder) ;
            return ApiResponse.<String>builder()
                    .code("200")
                    .status("success")
                    .message("File image uploaded successfully")
                    .data(url)
                    .build();
        }
}

