package vn.fu_ohayo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.FavoriteVocabulary;

import java.util.List;

@Repository
public interface FavoriteVocabularyRepository extends JpaRepository<FavoriteVocabulary, Integer> {
    Page<FavoriteVocabulary> findByIsPublicTrueAndUsers_UserIdNot(Long userId, Pageable pageable);
    List<FavoriteVocabulary> findByUsers_UserId(Long userId);
    List<FavoriteVocabulary> findByUsers_UserIdAndNameContainingIgnoreCase(Long userId, String name);
}
