package vn.fu_ohayo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.FavoriteGrammar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface FavoriteGrammarRepository extends JpaRepository<FavoriteGrammar, Integer> {
    Page<FavoriteGrammar> findByIsPublicTrueAndUsers_UserIdNot(Long userId, Pageable pageable);
    List<FavoriteGrammar> findByUsers_UserId(Long userId);
    List<FavoriteGrammar> findByUsers_UserIdAndNameContainingIgnoreCase(Long userId, String name);
}
