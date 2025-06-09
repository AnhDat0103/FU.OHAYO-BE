package vn.fu_ohayo.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.GrammarRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.GrammarResponse;
import vn.fu_ohayo.service.GrammarService;

@RestController
@RequestMapping("/grammars")
public class GrammarController {

    private final GrammarService grammarService;

    public GrammarController(GrammarService grammarService) {
        this.grammarService = grammarService;
    }

    @GetMapping
    public ApiResponse<Page<GrammarResponse>> getAllGrammars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam() int lessonId
    ) {
        Page<GrammarResponse> grammars = grammarService.getAllGrammars(lessonId, page, size);
        return ApiResponse.<Page<GrammarResponse>>builder()
                .status("success")
                .message("Fetched all grammars successfully")
                .data(grammars)
                .build();
    }

    @PostMapping
    public ApiResponse<GrammarResponse> createGrammar(@Valid @RequestBody GrammarRequest grammarRequest) {
        GrammarResponse createdGrammar = grammarService.saveGrammar(grammarRequest);
        return ApiResponse.<GrammarResponse>builder()
                .status("success")
                .message("Grammar created successfully")
                .data(createdGrammar)
                .build();
    }
}
