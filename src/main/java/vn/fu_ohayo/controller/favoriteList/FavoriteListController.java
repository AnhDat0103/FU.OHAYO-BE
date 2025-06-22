package vn.fu_ohayo.controller.favoriteList;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.AddFavoriteFolderRequest;
import vn.fu_ohayo.dto.request.FavoriteListRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.FolderFavoriteResponse;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.service.FavoriteListService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteListController {

    private final FavoriteListService favoriteListService;


    @GetMapping
    public ApiResponse<Page<FolderFavoriteResponse>> getAllFolders(@ModelAttribute FavoriteListRequest favoriteListRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        return ApiResponse.<Page<FolderFavoriteResponse>>builder()
                .code("200")
                .status("success")
                .message("Fetched folders successfully")
                .data(favoriteListService.getAllFavoriteLists(username, favoriteListRequest))
                .build();
    }

    @PostMapping
    public ApiResponse<?> createFavoriteFolder(@Valid @RequestBody AddFavoriteFolderRequest request) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                User user = (User) auth.getPrincipal();
        favoriteListService.createFavoriteFolder(user, request);
        return ApiResponse.builder()
                .code("201")
                .status("success")
                .message("Created folder successfully")
                .build();
    }

    @PostMapping("/copy")
    public ApiResponse<?> copyFolder(@Valid @RequestBody Long folderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        favoriteListService.copyFavoriteFolder(user, folderId);

        return ApiResponse.builder()
                .code("201")
                .status("success")
                .message("Copied folder successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<?> updateFavoriteFolder(@PathVariable Long id,
                                               @RequestBody AddFavoriteFolderRequest request) {
        favoriteListService.updateFavoriteFolder(id, request);
        return ApiResponse.builder()
                .code("200")
                .status("success")
                .message("Updated folder successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteFavoriteFolder(@PathVariable Long id) {
        favoriteListService.deleteFavoriteFolder(id);
        return ApiResponse.builder()
                .code("200")
                .status("success")
                .message("Deleted folder successfully")
                .build();
    }


}
