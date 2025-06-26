package vn.fu_ohayo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.AddFavoriteFolderRequest;
import vn.fu_ohayo.dto.request.FavoriteListRequest;
import vn.fu_ohayo.dto.response.FolderFavoriteResponse;
import vn.fu_ohayo.entity.FavoriteList;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.repository.FavoriteListRepository;
import vn.fu_ohayo.repository.GrammarRepository;
import vn.fu_ohayo.repository.VocabularyRepository;
import vn.fu_ohayo.service.FavoriteListService;

import java.util.Date;
import java.util.HashSet;

@RequiredArgsConstructor
@Service
public class FavoriteListServiceImp implements FavoriteListService {

    private final FavoriteListRepository favoriteListRepository;

    public void copyFavoriteFolder(User user, Long folderId) {
        FavoriteList original = favoriteListRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        if (!original.isPublic()) {
            throw new RuntimeException("You can only copy public folders.");
        }

        FavoriteList copy = FavoriteList.builder()
                .favoriteListName(original.getFavoriteListName())
                .user(user)
                .isPublic(original.isPublic())
                .isDeleted(original.isDeleted())
                .createdAt(new Date())
                .isDeleted(false)
                .vocabularies(new HashSet<>(original.getVocabularies()))
                .grammars(new HashSet<>(original.getGrammars()))
                .build();
        favoriteListRepository.save(copy);
    }

    @Override
    public Page<FolderFavoriteResponse> getAllFavoriteLists(String username, FavoriteListRequest request) {
        int page = request.getCurrentPage() != null ? request.getCurrentPage() : 0;
        int size = request.getPageSize() != null ? request.getPageSize() : 21;
        Pageable pageable = PageRequest.of(page, size);
        String keyword = request.getSearchName() != null ? request.getSearchName() : "";
        String type = request.getType(); // NEW: "vocabulary", "grammar", or "all"
        String view = request.getViewType(); // NEW: "mine" or "public"
        Boolean isPublic = request.getIsPublic();

        Page<FavoriteList> pageResult;

        if ("public".equalsIgnoreCase(view)) {
            if ("vocabulary".equalsIgnoreCase(type)) {
                pageResult = favoriteListRepository.findPublicVocabularyFoldersExcludeUser(username, keyword, pageable);
            } else if ("grammar".equalsIgnoreCase(type)) {
                pageResult = favoriteListRepository.findPublicGrammarFoldersExcludeUser(username, keyword, pageable);
            } else {
                pageResult = favoriteListRepository.findPublicFoldersExcludeUser(username, keyword, pageable);
            }
        } else {
            // Mặc định là "mine"
            if (isPublic != null) {
                if ("vocabulary".equalsIgnoreCase(type)) {
                    pageResult = favoriteListRepository.findMyVocabularyFoldersWithPublic(username, keyword, isPublic, pageable);
                } else if ("grammar".equalsIgnoreCase(type)) {
                    pageResult = favoriteListRepository.findMyGrammarFoldersWithPublic(username, keyword, isPublic, pageable);
                } else {
                    pageResult = favoriteListRepository.findMyAllFoldersWithPublic(username, keyword, isPublic, pageable);
                }
            } else {
                if ("vocabulary".equalsIgnoreCase(type)) {
                    pageResult = favoriteListRepository.findVocabularyFolders(username, keyword, pageable);
                } else if ("grammar".equalsIgnoreCase(type)) {
                    pageResult = favoriteListRepository.findGrammarFolders(username, keyword, pageable);
                } else {
                    pageResult = favoriteListRepository.findAllFolders(username, keyword, pageable);
                }
            }
        }

        return pageResult.map(fav -> {
            long vocCount = favoriteListRepository.countVocabularies(fav.getFavoriteId());
            long graCount = favoriteListRepository.countGrammars(fav.getFavoriteId());

            return FolderFavoriteResponse.builder()
                    .id(fav.getFavoriteId())
                    .name(fav.getFavoriteListName())
                    .isPublic(fav.isPublic())
                    .addedAt(fav.getCreatedAt())
                    .ownerName(fav.getUser().getFullName())
                    .numberOfFavorites((int) (vocCount + graCount))
                    .build();
        });
    }

    @Override
    public void createFavoriteFolder(User user, AddFavoriteFolderRequest request) {
        FavoriteList favoriteList = new FavoriteList();
        favoriteList.setFavoriteListName(request.getName());
        favoriteList.setPublic(request.getIsPublic());
        favoriteList.setUser(user);
        favoriteListRepository.save(favoriteList);
    }

    @Override
    public void updateFavoriteFolder(Long id, AddFavoriteFolderRequest request) {
        FavoriteList folder = favoriteListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found"));
        folder.setFavoriteListName(request.getName());
        folder.setPublic(request.getIsPublic());
        favoriteListRepository.save(folder);
    }

    @Override
    public void deleteFavoriteFolder(Long id) {
        FavoriteList folder = favoriteListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found"));
        folder.setDeleted(true);
        favoriteListRepository.save(folder);
    }

}
