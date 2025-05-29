package vn.fu_ohayo.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.VocabularyResponse;
import vn.fu_ohayo.service.VocabularyService;


@RestController
@RequestMapping("/vocabularies")
public class VocabularyController {

    private final VocabularyService vocabularyService;

    public VocabularyController(VocabularyService vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    @GetMapping
    public ApiResponse<Page<VocabularyResponse>> getAllVocabularies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam int lessonId
    ) {
        return ApiResponse.<Page<VocabularyResponse>>builder()
                .code("200")
                .message("Get all vocabularies successfully")
                .status("success")
                .data(vocabularyService.getVocabularyPage(page, size, lessonId))
                .build();
    }
}
