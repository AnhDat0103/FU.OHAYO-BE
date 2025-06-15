package vn.fu_ohayo.controller.favorite;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.AddFolderFavoriteRequest;
import vn.fu_ohayo.dto.request.AddUserRequest;
import vn.fu_ohayo.dto.request.UpdateFolderFavoriteRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.FolderFavoriteResponse;
import vn.fu_ohayo.dto.response.UserResponse;
import vn.fu_ohayo.repository.FavoriteGrammarRepository;
import vn.fu_ohayo.repository.FavoriteVocabularyRepository;
import vn.fu_ohayo.service.FavoriteGrammarService;
import vn.fu_ohayo.service.FavoriteVocabularyService;
import vn.fu_ohayo.service.GrammarService;
import vn.fu_ohayo.service.VocabularyService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteVocabularyService favoriteVocabularyService;
    private final FavoriteGrammarService favoriteGrammarService;

    @GetMapping("/grammarfolders")
    public ApiResponse<List<FolderFavoriteResponse>> getAllGrammarFavorite() {
        return ApiResponse.<List<FolderFavoriteResponse>>builder()
                .code("200")
                .status("success")
                .message("Get all folder grammar successfully")
                .data(favoriteGrammarService.getAllFolderFavoriteGrammar())
                .build();
    }

    @PostMapping("/folders")
    public ApiResponse<FolderFavoriteResponse> addFavoriteFolder(@RequestBody @Valid AddFolderFavoriteRequest request) {
        FolderFavoriteResponse response;
        if ("vocabulary".equalsIgnoreCase(request.getType())) {
            response = favoriteVocabularyService.createFolder(request);
        } else if ("grammar".equalsIgnoreCase(request.getType())) {
            response = favoriteGrammarService.createFolder(request);
        } else {
            throw new IllegalArgumentException("Invalid folder type: must be 'vocabulary' or 'grammar'");
        }

        return ApiResponse.<FolderFavoriteResponse>builder()
                .code("200")
                .status("success")
                .message("Folder created successfully")
                .data(response)
                .build();
    }

    @PutMapping("/folders/{id}")
    public ApiResponse<FolderFavoriteResponse> updateFavoriteFolder(
            @PathVariable Integer id,
            @RequestParam String type,
            @RequestBody @Valid UpdateFolderFavoriteRequest request
    ) {
        FolderFavoriteResponse response;
        if ("vocabulary".equalsIgnoreCase(type)) {
            response = favoriteVocabularyService.updateFolder(id, request);
        } else if ("grammar".equalsIgnoreCase(type)) {
            response = favoriteGrammarService.updateFolder(id, request);
        } else {
            throw new IllegalArgumentException("Invalid folder type: must be 'vocabulary' or 'grammar'");
        }

        return ApiResponse.<FolderFavoriteResponse>builder()
                .code("200")
                .status("success")
                .message("Folder updated successfully")
                .data(response)
                .build();
    }

    @DeleteMapping("/folders/{id}")
    public ApiResponse<String> deleteFavoriteFolder(
            @PathVariable Integer id,
            @RequestParam String type
    ) {
        if ("vocabulary".equalsIgnoreCase(type)) {
            favoriteVocabularyService.deleteFolder(id);
        } else if ("grammar".equalsIgnoreCase(type)) {
            favoriteGrammarService.deleteFolder(id);
        } else {
            throw new IllegalArgumentException("Invalid folder type: must be 'vocabulary' or 'grammar'");
        }

        return ApiResponse.<String>builder()
                .code("200")
                .status("success")
                .message("Folder deleted successfully")
                .data("Deleted folder with id " + id)
                .build();
    }

    @GetMapping("/publicfolders/search")
    public ApiResponse<List<FolderFavoriteResponse>> searchPublicFolders(
            @RequestParam String type,
            @RequestParam(required = false) String keyword
    ) {
        List<FolderFavoriteResponse> result;

        if ("vocabulary".equalsIgnoreCase(type)) {
            result = favoriteVocabularyService.searchPublicVocabularyFolders(keyword);
        } else if ("grammar".equalsIgnoreCase(type)) {
            result = favoriteGrammarService.searchPublicGrammarFolders(keyword);
        } else {
            throw new IllegalArgumentException("Invalid folder type: must be 'vocabulary' or 'grammar'");
        }

        return ApiResponse.<List<FolderFavoriteResponse>>builder()
                .code("200")
                .status("success")
                .message("Search public folders successfully")
                .data(result)
                .build();
    }



    @GetMapping("/folders")
    public ApiResponse<List<FolderFavoriteResponse>> searchFolders(
            @RequestParam String type,
            @RequestParam(required = false) String keyword
    ) {
        List<FolderFavoriteResponse> result;
        if ("vocabulary".equalsIgnoreCase(type)) {
            result = favoriteVocabularyService.searchFavoriteVocabularyFoldersByUserAndName(keyword);
        } else if ("grammar".equalsIgnoreCase(type)) {
            result = favoriteGrammarService.searchFavoriteGrammarFoldersByUserAndName(keyword);
        } else {
            throw new IllegalArgumentException("Invalid folder type: must be 'vocabulary' or 'grammar'");
        }

        return ApiResponse.<List<FolderFavoriteResponse>>builder()
                .code("200")
                .status("success")
                .message("Search folder successfully")
                .data(result)
                .build();
    }


    @GetMapping("/vocabularyfolders")
    public ApiResponse<List<FolderFavoriteResponse>> getAllVocabularyFavorite() {
        return ApiResponse.<List<FolderFavoriteResponse>>builder()
                .code("200")
                .status("success")
                .message("Get all folder vocabulary successfully")
                .data(favoriteVocabularyService.getAllFolderFavoriteVocabulary())
                .build();
    }

    @GetMapping("/publicfolders")
    public ApiResponse<Page<FolderFavoriteResponse>> getPublicFolders(
            @RequestParam String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "21") int size
    ) {
        Page<FolderFavoriteResponse> result;

        if ("vocabulary".equalsIgnoreCase(type)) {
            result = favoriteVocabularyService.getPublicVocabularyFolders(page, size);
        } else if ("grammar".equalsIgnoreCase(type)) {
            result = favoriteGrammarService.getPublicGrammarFolders(page, size);
        } else {
            throw new IllegalArgumentException("Invalid folder type: must be 'vocabulary' or 'grammar'");
        }

        return ApiResponse.<Page<FolderFavoriteResponse>>builder()
                .code("200")
                .status("success")
                .message("Fetched public folders successfully")
                .data(result)
                .build();
    }



}
