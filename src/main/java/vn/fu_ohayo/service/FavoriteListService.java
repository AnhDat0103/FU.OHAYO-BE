package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.request.AddFavoriteFolderRequest;
import vn.fu_ohayo.dto.request.FavoriteListRequest;
import vn.fu_ohayo.dto.response.FolderFavoriteResponse;
import vn.fu_ohayo.entity.User;


public interface FavoriteListService {
    public void copyFavoriteFolder(User user, Long folderId);
    Page<FolderFavoriteResponse> getAllFavoriteLists(String username, FavoriteListRequest request);
    void createFavoriteFolder(User username, AddFavoriteFolderRequest request);
    void updateFavoriteFolder(Long id, AddFavoriteFolderRequest request);
    void deleteFavoriteFolder(Long id);
}
