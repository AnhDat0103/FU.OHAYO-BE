package vn.fu_ohayo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.AddFolderFavoriteRequest;
import vn.fu_ohayo.dto.request.UpdateFolderFavoriteRequest;
import vn.fu_ohayo.dto.response.FolderFavoriteResponse;
import vn.fu_ohayo.entity.FavoriteGrammar;
import vn.fu_ohayo.repository.FavoriteGrammarRepository;
import vn.fu_ohayo.repository.GrammarRepository;
import vn.fu_ohayo.service.FavoriteGrammarService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FavoriteGrammarServiceImp implements FavoriteGrammarService {

    private final FavoriteGrammarRepository favoriteGrammarRepository;
    private final GrammarRepository grammarRepository;


    @Override
    public List<FolderFavoriteResponse> searchPublicGrammarFolders(String keyword) {
        Long currentUserId = 1L;

        List<FavoriteGrammar> grammars = favoriteGrammarRepository
                .findByIsPublicTrueAndUsers_UserIdNotAndNameContainingIgnoreCase(currentUserId, keyword);

        return grammars.stream().map(fg -> FolderFavoriteResponse.builder()
                .id(fg.getId())
                .name(fg.getName())
                .isPublic(fg.isPublic())
                .ownerName(fg.getOwnerName())
                .addedAt(fg.getAddedAt())
                .numberOfFavorites(grammarRepository.countByFavoriteGrammarId(fg.getId()))
                .build()
        ).toList();
    }


    @Override
    public Page<FolderFavoriteResponse> getPublicGrammarFolders(int page, int size) {
        Long currentUserId = 1L;

        Pageable pageable = PageRequest.of(page, size);
        Page<FavoriteGrammar> grammarPage = favoriteGrammarRepository.findByIsPublicTrueAndUsers_UserIdNot(currentUserId, pageable);

        return grammarPage.map(fv -> FolderFavoriteResponse.builder()
                .id(fv.getId())
                .name(fv.getName())
                .isPublic(fv.isPublic())
                .ownerName(fv.getOwnerName())
                .addedAt(fv.getAddedAt())
                .numberOfFavorites(grammarRepository.countByFavoriteGrammarId(fv.getId()))
                .build());
    }

    @Override
    public List<FolderFavoriteResponse> searchFavoriteGrammarFoldersByUserAndName(String keyword) {
        Long userId = 1L;

        List<FavoriteGrammar> favoriteGrammars;

        if (keyword == null || keyword.trim().isEmpty()) {
            favoriteGrammars = favoriteGrammarRepository.findByUsers_UserId(userId);
        } else {
            favoriteGrammars = favoriteGrammarRepository.findByUsers_UserIdAndNameContainingIgnoreCase(userId, keyword.trim());
        }

        List<FolderFavoriteResponse> responses = new ArrayList<>();
        for (FavoriteGrammar fv : favoriteGrammars) {
            FolderFavoriteResponse response = new FolderFavoriteResponse();
            response.setId(fv.getId());
            response.setName(fv.getName());
            response.setPublic(fv.isPublic());
            response.setAddedAt(fv.getAddedAt());
            response.setOwnerName(fv.getOwnerName());
            response.setNumberOfFavorites(grammarRepository.countByFavoriteGrammarId(response.getId()));
            responses.add(response);
        }

        return responses;
    }

    @Override
    public FolderFavoriteResponse updateFolder(Integer id, UpdateFolderFavoriteRequest request) {
        FavoriteGrammar folder = favoriteGrammarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        folder.setName(request.getName());
        folder.setPublic(request.getIsPublic());

        favoriteGrammarRepository.save(folder);

        return FolderFavoriteResponse.builder()
                .id(folder.getId())
                .name(folder.getName())
                .isPublic(folder.isPublic())
                .ownerName(folder.getOwnerName())
                .addedAt(folder.getAddedAt())
                .numberOfFavorites(grammarRepository.countByFavoriteGrammarId(folder.getId()))
                .build();
    }

    @Override
    public void deleteFolder(Integer id) {
        FavoriteGrammar folder = favoriteGrammarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grammar folder not found with id: " + id));

        folder.setDeleted(true);
        favoriteGrammarRepository.save(folder);
    }



    @Override
    public List<FolderFavoriteResponse> getAllFolderFavoriteGrammar() {
        Long userId = 1L;
        List<FavoriteGrammar> favoriteGrammars = favoriteGrammarRepository.findByUsers_UserId(userId);
        List<FolderFavoriteResponse> responses = new ArrayList<>();
        for (FavoriteGrammar favoriteGrammar : favoriteGrammars) {
            FolderFavoriteResponse response = new FolderFavoriteResponse();
            response.setId(favoriteGrammar.getId());
            response.setName(favoriteGrammar.getName());
            response.setPublic(favoriteGrammar.isPublic());
            response.setAddedAt(favoriteGrammar.getAddedAt());
            response.setOwnerName(favoriteGrammar.getOwnerName());
            response.setNumberOfFavorites(grammarRepository.countByFavoriteGrammarId(response.getId()));
            responses.add(response);
        }
        return responses;
    }

    @Override
    public FolderFavoriteResponse createFolder(AddFolderFavoriteRequest request) {
        FavoriteGrammar favoriteGrammar = FavoriteGrammar.builder()
                .name(request.getName())
                .isPublic(request.getIsPublic())
                .ownerName("default_user")
                .build();

        favoriteGrammar = favoriteGrammarRepository.save(favoriteGrammar);

        return FolderFavoriteResponse.builder()
                .id(favoriteGrammar.getId())
                .name(favoriteGrammar.getName())
                .isPublic(favoriteGrammar.isPublic())
                .ownerName(favoriteGrammar.getOwnerName())
                .addedAt(favoriteGrammar.getAddedAt())
                .numberOfFavorites(0)
                .build();
    }


}
