package vn.fu_ohayo.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.AddFolderFavoriteRequest;
import vn.fu_ohayo.dto.request.UpdateFolderFavoriteRequest;
import vn.fu_ohayo.dto.response.FolderFavoriteResponse;
import vn.fu_ohayo.entity.FavoriteGrammar;
import vn.fu_ohayo.entity.FavoriteVocabulary;
import vn.fu_ohayo.repository.FavoriteGrammarRepository;
import vn.fu_ohayo.repository.FavoriteVocabularyRepository;
import vn.fu_ohayo.repository.GrammarRepository;
import vn.fu_ohayo.repository.VocabularyRepository;
import vn.fu_ohayo.service.FavoriteVocabularyService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FavoriteVocabularyServiceImp implements FavoriteVocabularyService {
    private final FavoriteVocabularyRepository favoriteVocabularyRepository;
    private final VocabularyRepository vocabularyRepository;



    @Override
    public Page<FolderFavoriteResponse> getPublicVocabularyFolders(int page, int size) {
        Long currentUserId = 1L;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "addedAt"));
        Page<FavoriteVocabulary> publicFolders = favoriteVocabularyRepository
                .findByIsPublicTrueAndUsers_UserIdNot(currentUserId, pageable);

        return publicFolders.map(fv -> FolderFavoriteResponse.builder()
                .id(fv.getId())
                .name(fv.getName())
                .isPublic(fv.isPublic())
                .ownerName(fv.getOwnerName())
                .addedAt(fv.getAddedAt())
                .numberOfFavorites(vocabularyRepository.countByFavoriteVocabularyId(fv.getId()))
                .build()
        );
    }

    @Override
    public List<FolderFavoriteResponse> searchFavoriteVocabularyFoldersByUserAndName(String keyword) {
        Long userId = 1L;

        List<FavoriteVocabulary> favoriteVocabularies;

        if (keyword == null || keyword.trim().isEmpty()) {
            favoriteVocabularies = favoriteVocabularyRepository.findByUsers_UserId(userId);
        } else {
            favoriteVocabularies = favoriteVocabularyRepository.findByUsers_UserIdAndNameContainingIgnoreCase(userId, keyword.trim());
        }

        List<FolderFavoriteResponse> responses = new ArrayList<>();
        for (FavoriteVocabulary fv : favoriteVocabularies) {
            FolderFavoriteResponse response = new FolderFavoriteResponse();
            response.setId(fv.getId());
            response.setName(fv.getName());
            response.setPublic(fv.isPublic());
            response.setAddedAt(fv.getAddedAt());
            response.setOwnerName(fv.getOwnerName());
            response.setNumberOfFavorites(vocabularyRepository.countByFavoriteVocabularyId(fv.getId()));
            responses.add(response);
        }

        return responses;
    }

    @Override
    public FolderFavoriteResponse updateFolder(Integer id, UpdateFolderFavoriteRequest request) {
        FavoriteVocabulary folder = favoriteVocabularyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        folder.setName(request.getName());
        folder.setPublic(request.getIsPublic());

        favoriteVocabularyRepository.save(folder);

        return FolderFavoriteResponse.builder()
                .id(folder.getId())
                .name(folder.getName())
                .isPublic(folder.isPublic())
                .ownerName(folder.getOwnerName())
                .addedAt(folder.getAddedAt())
                .numberOfFavorites(vocabularyRepository.countByFavoriteVocabularyId(folder.getId()))
                .build();
    }

    @Override
    public void deleteFolder(Integer id) {
        FavoriteVocabulary folder = favoriteVocabularyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found"));
        favoriteVocabularyRepository.delete(folder);
    }


    @Override
    public List<FolderFavoriteResponse> getAllFolderFavoriteVocabulary() {
        Long userId = 1L;
        List<FavoriteVocabulary> favoriteGrammars = favoriteVocabularyRepository.findByUsers_UserId(userId);
        List<FolderFavoriteResponse> responses = new ArrayList<>();
        for (FavoriteVocabulary favoriteVocabulary : favoriteGrammars) {
            FolderFavoriteResponse response = new FolderFavoriteResponse();
            response.setId(favoriteVocabulary.getId());
            response.setName(favoriteVocabulary.getName());
            response.setPublic(favoriteVocabulary.isPublic());
            response.setAddedAt(favoriteVocabulary.getAddedAt());
            response.setOwnerName(favoriteVocabulary.getOwnerName());
            response.setNumberOfFavorites(vocabularyRepository.countByFavoriteVocabularyId(response.getId()));
            responses.add(response);
        }
        return responses;
    }

    @Override
    public FolderFavoriteResponse createFolder(AddFolderFavoriteRequest request) {
        FavoriteVocabulary favoriteVocabulary = FavoriteVocabulary.builder()
                .name(request.getName())
                .isPublic(request.getIsPublic())
                .ownerName("default_user")
                .build();

        favoriteVocabulary = favoriteVocabularyRepository.save(favoriteVocabulary);

        return FolderFavoriteResponse.builder()
                .id(favoriteVocabulary.getId())
                .name(favoriteVocabulary.getName())
                .isPublic(favoriteVocabulary.isPublic())
                .ownerName(favoriteVocabulary.getOwnerName())
                .addedAt(favoriteVocabulary.getAddedAt())
                .numberOfFavorites(0)
                .build();
    }

}
