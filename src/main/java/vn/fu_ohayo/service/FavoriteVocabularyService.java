package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.request.AddFolderFavoriteRequest;
import vn.fu_ohayo.dto.request.UpdateFolderFavoriteRequest;
import vn.fu_ohayo.dto.response.FolderFavoriteResponse;

import java.util.List;

public interface FavoriteVocabularyService {
    List<FolderFavoriteResponse> searchPublicVocabularyFolders(String keyword);
    Page<FolderFavoriteResponse> getPublicVocabularyFolders(int page, int size);
    void deleteFolder(Integer id);
    FolderFavoriteResponse updateFolder(Integer id, UpdateFolderFavoriteRequest request);
    public List<FolderFavoriteResponse> searchFavoriteVocabularyFoldersByUserAndName(String keyword);
    List<FolderFavoriteResponse> getAllFolderFavoriteVocabulary();
    FolderFavoriteResponse createFolder(AddFolderFavoriteRequest request);
}
