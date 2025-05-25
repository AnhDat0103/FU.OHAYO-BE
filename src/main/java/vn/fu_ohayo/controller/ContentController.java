package vn.fu_ohayo.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import vn.fu_ohayo.dto.request.ContentSpeakingRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.entity.Content;
import vn.fu_ohayo.entity.ContentSpeaking;
import vn.fu_ohayo.entity.Dialogue;
import vn.fu_ohayo.enums.ContentTypeEnum;
import vn.fu_ohayo.repository.ContentRepository;
import vn.fu_ohayo.repository.ContentSpeakingRepository;
import vn.fu_ohayo.service.ContentService;
import vn.fu_ohayo.service.ContentSpeakingService;
import vn.fu_ohayo.service.DialogueService;

import java.util.List;


@RestController
@RequestMapping("/contents")
public class ContentController {
    private final ContentService contentService;
    private final ContentSpeakingService contentSpeakingService;
    private final DialogueService dialogueService;
    private final ContentRepository contentRepository;
    private final ContentSpeakingRepository contentSpeakingRepository;

    public ContentController(ContentService contentService, ContentSpeakingService contentSpeakingService, DialogueService dialogueService, ContentRepository contentRepository, ContentSpeakingRepository contentSpeakingRepository) {
        this.contentService = contentService;
        this.contentSpeakingService = contentSpeakingService;
        this.dialogueService = dialogueService;
        this.contentRepository = contentRepository;
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
    public ApiResponse<ContentSpeaking> updateContentSpeaking(
            @PathVariable Long id,
            @RequestBody ContentSpeakingRequest request) {

        ContentSpeaking contentSpeaking = contentSpeakingService.getContentSpeakingById(id);
        contentSpeaking.setTitle(request.getTitle());
        contentSpeaking.setImage(request.getImage());
        contentSpeaking.setCategory(request.getCategory());
        contentSpeakingService.handleSaveContentSpeaking(contentSpeaking);
        return ApiResponse.<ContentSpeaking>builder()
                .code("200")
                .status("success")
                .message("Updated all fields of content speaking")
                .data(contentSpeaking)
                .build();
    }

    @PatchMapping("/{id")
    public  ApiResponse<ContentSpeaking> patchContentSpeaking(
            @PathVariable Long id,
            @RequestBody ContentSpeakingRequest request){
        ContentSpeaking contentSpeaking = contentSpeakingService.getContentSpeakingById(id);
        if (request.getTitle() != null) {
            contentSpeaking.setTitle(request.getTitle());
        }
        if (request.getImage() != null) {
            contentSpeaking.setImage(request.getImage());
        }
        if (request.getCategory() != null) {
            contentSpeaking.setCategory(request.getCategory());
        }
        ContentSpeaking updated = contentSpeakingService.handleSaveContentSpeaking(contentSpeaking);
        return ApiResponse.<ContentSpeaking>builder()
                .code("200")
                .status("success")
                .message("Patched content speaking fields")
                .data(updated)
                .build();
    }
}
