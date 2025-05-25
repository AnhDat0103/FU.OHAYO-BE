package vn.fu_ohayo.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.fu_ohayo.dto.request.ContentSpeakingRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.ContentSpeakingResponse;
import vn.fu_ohayo.entity.ContentSpeaking;
import vn.fu_ohayo.repository.ContentSpeakingRepository;
import vn.fu_ohayo.service.ContentSpeakingService;
import java.util.List;


@RestController
@RequestMapping("/contents")
public class ContentController {
    private final ContentSpeakingService contentSpeakingService;
    private final ContentSpeakingRepository contentSpeakingRepository;

    public ContentController( ContentSpeakingService contentSpeakingService, ContentSpeakingRepository contentSpeakingRepository) {
        this.contentSpeakingService = contentSpeakingService;
        this.contentSpeakingRepository = contentSpeakingRepository;
    }

    @GetMapping
    public ApiResponse<List<ContentSpeaking>> getContentSpeakings() {
        return ApiResponse .<List<ContentSpeaking>>builder()
                .code("200")
                .status("success")
                .message("success")
                .data(contentSpeakingRepository.findAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ContentSpeaking> getContentSpeaking(@PathVariable  int id) {
        return  ApiResponse.<ContentSpeaking>builder()
                .code("200")
                .status("success")
                .message("Get contentSpeaking by id")
                .data(contentSpeakingService.getContentSpeakingById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<ContentSpeaking> createContentSpeaking(@Valid @RequestBody ContentSpeakingRequest request) {
        ContentSpeaking newContentSpeaking =contentSpeakingService.handleCreateContentSpeaking(request);
        return ApiResponse.<ContentSpeaking>builder()
                .code("201")
                .status("success")
                .message("Created new ContentSpeaking successfully")
                .data(newContentSpeaking)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<ContentSpeaking> deleteContentSpeaking(@PathVariable long id) {
        contentSpeakingService.deleteContentSpeakingById(id);
        return ApiResponse.<ContentSpeaking>builder()
                .code("200")
                .status("success")
                .message("Deleted content speaking successfully")
                .build();

    }

    @PutMapping("/{id}")
    public ApiResponse<ContentSpeakingResponse> updateContentSpeaking(
            @PathVariable Long id,
            @RequestBody ContentSpeakingRequest request) {
        ContentSpeakingResponse contentSpeakingResponse = contentSpeakingService.updatePutContentSpeaking(id,request );
        return ApiResponse.<ContentSpeakingResponse>builder()
                .code("200")
                .status("success")
                .message("Updated all fields of content speaking")
                .data(contentSpeakingResponse)
                .build();
    }

    @PatchMapping("/{id}")
    public  ApiResponse<ContentSpeakingResponse> patchContentSpeaking(
            @PathVariable Long id,
            @RequestBody ContentSpeakingRequest request){
        ContentSpeakingResponse ContentSpeakingResponse = contentSpeakingService.updatePatchContentSpeaking(id,request );
        return ApiResponse.<ContentSpeakingResponse>builder()
                .code("200")
                .status("success")
                .message("Updated all fields of content speaking")
                .data(ContentSpeakingResponse)
                .build();
    }
}
