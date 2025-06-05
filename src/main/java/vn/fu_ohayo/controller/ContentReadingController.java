package vn.fu_ohayo.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.ContentReadingRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.ContentReadingResponse;
import vn.fu_ohayo.entity.ContentReading;
import vn.fu_ohayo.service.ContentReadingService;


@RestController
@RequestMapping("/content_reading")
public class ContentReadingController {
    private final ContentReadingService contentReadingService;
    String success = "success";
    public ContentReadingController(ContentReadingService contentReadingService) {
        this.contentReadingService = contentReadingService;
    }

    @GetMapping()
    public ApiResponse<Page<ContentReadingResponse>> getContentReadingPage(
            @RequestParam(defaultValue="1" ) int page,
            @RequestParam(defaultValue = "5") int size                                                                             ) {
        return ApiResponse .    <Page<ContentReadingResponse>>builder()
                .code("200")
                .status(success)
                .message(success)
                .data(contentReadingService.getContentReadingPage(page, size))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ContentReading> getContentReading(@PathVariable long id) {
        return  ApiResponse.<ContentReading>builder()
                .code("200")
                .status(success)
                .message("Get contentReading by id")
                .data(contentReadingService.getContentReadingById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<ContentReading> createContentReading(@Valid @RequestBody ContentReadingRequest request) {
        ContentReading newContentReading =contentReadingService.handleCreateContentReading(request);
        return ApiResponse.<ContentReading>builder()
                .code("201")
                .status(success)
                .message("Created new ContentReading successfully")
                .data(newContentReading)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<ContentReading> deleteContentReading(@PathVariable long id) {
        contentReadingService.deleteContentReadingById(id);
        return ApiResponse.<ContentReading>builder()
                .code("200")
                .status(success)
                .message("Deleted content reading successfully")
                .build();

    }

    @PatchMapping("/{id}")
    public  ApiResponse<ContentReadingResponse> patchContentReading(
            @PathVariable Long id,
            @Valid @RequestBody ContentReadingRequest request){
        ContentReadingResponse contentReadingResponse = contentReadingService.updatePatchContentReading(id,request );
        return ApiResponse.<ContentReadingResponse>builder()
                .code("200")
                .status(success)
                .message("Updated all fields of content reading")
                .data(contentReadingResponse)
                .build();
    }
}
