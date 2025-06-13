package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.request.AddFolderFavoriteRequest;
import vn.fu_ohayo.dto.request.UpdateFolderFavoriteRequest;
import vn.fu_ohayo.dto.response.FolderFavoriteResponse;

import java.util.List;


public interface FavoriteGrammarService {
    Page<FolderFavoriteResponse> getPublicGrammarFolders(int page, int size);
    void deleteFolder(Integer id);
    FolderFavoriteResponse updateFolder(Integer id, UpdateFolderFavoriteRequest request);
    public List<FolderFavoriteResponse> searchFavoriteGrammarFoldersByUserAndName(String keyword);
    List<FolderFavoriteResponse> getAllFolderFavoriteGrammar();
    FolderFavoriteResponse createFolder(AddFolderFavoriteRequest request);
}
