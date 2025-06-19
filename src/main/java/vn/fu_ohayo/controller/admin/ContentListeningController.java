package vn.fu_ohayo.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.ContentListeningRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.ContentListeningResponse;
import vn.fu_ohayo.entity.ContentListening;
import vn.fu_ohayo.mapper.ContentMapper;
import vn.fu_ohayo.mapper.LessonMapper;
import vn.fu_ohayo.service.ContentListeningService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/content_listening")
public class ContentListeningController {
    private final ContentListeningService contentListeningService;
    private final ContentMapper contentMapper;

    @GetMapping()
    public ApiResponse<Page<ContentListeningResponse>> getContentListeningPage(
            @RequestParam(defaultValue="1" ) int page,
            @RequestParam(defaultValue = "5") int size                                                                             ) {
        return ApiResponse .    <Page<ContentListeningResponse>>builder()
                .code("200")
                .status("success")
                .message("success")
                .data(contentListeningService.getContentListeningPage(page, size))
                .build();
    }

    @GetMapping("/details/{id}")
    public ApiResponse<ContentListeningResponse> getContentListening(@PathVariable long id) {
        ContentListeningResponse contentListeningResponse = contentMapper.toContentListeningResponse(contentListeningService.getContentListeningById(id));
        return  ApiResponse.<ContentListeningResponse>builder()
                .code("200")
                .status("success")
                .message("Get contentListening by id")
                .data(contentListeningResponse)
                .build();
    }

    @PostMapping
    public ApiResponse<ContentListening> createContentListening(@Valid @RequestBody ContentListeningRequest request) {
        ContentListening newContentListening =contentListeningService.handleCreateContentListening(request);
        return ApiResponse.<ContentListening>builder()
                .code("201")
                .status("success")
                .message("Created new ContentListening successfully")
                .data(newContentListening)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<ContentListening> deleteContentListening(@PathVariable long id) {
        contentListeningService.deleteContentListeningById(id);
        return ApiResponse.<ContentListening>builder()
                .code("200")
                .status("success")
                .message("Deleted content listening successfully")
                .build();

    }

    @PatchMapping("/{id}")
    public  ApiResponse<ContentListeningResponse> patchContentListening(
            @PathVariable Long id,
            @Valid @RequestBody ContentListeningRequest request){
        ContentListeningResponse contentListeningResponse = contentListeningService.updatePatchContentListening(id,request );
        return ApiResponse.<ContentListeningResponse>builder()
                .code("200")
                .status("success")
                .message("Updated some fields of content listening")
                .data(contentListeningResponse)
                .build();
    }
}

