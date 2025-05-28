package vn.fu_ohayo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.enums.CategoryListeningEnum;
import vn.fu_ohayo.enums.CategoryReadingEnum;
import vn.fu_ohayo.enums.CategorySpeakingEnum;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/content_category")
public class ContentCategoryController {
    @GetMapping("/{category}")
    public ApiResponse<List<String>> getContentCategory(@PathVariable String category) {
        List<String> categories ;
        String message = "Fetched content categories successfully";
        String code = "200";
        String status = "success";
        if(category.equalsIgnoreCase("speaking")) {
            categories = Arrays.stream(CategorySpeakingEnum.values()).map(Enum::name).collect(Collectors.toList());
        } else if(category.equalsIgnoreCase("listening")) {
            categories = Arrays.stream(CategoryListeningEnum.values()).map(Enum::name).collect(Collectors.toList());
        } else if(category.equalsIgnoreCase("reading")) {
            categories = Arrays.stream(CategoryReadingEnum.values()).map(Enum::name).collect(Collectors.toList());
        } else {
            categories = Arrays.asList("Unknown category");
            message = "Category not found";
            code = "404";
            status = "error";
        }
        return ApiResponse.<List<String>>builder()
                .code(code)
                .status(status)
                .message(message)
                .data(categories)
                .build();
    }
}
