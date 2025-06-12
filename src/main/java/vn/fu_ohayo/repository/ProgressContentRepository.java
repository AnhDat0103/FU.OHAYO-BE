package vn.fu_ohayo.repository;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.fu_ohayo.entity.ProgressContent;
import java.util.List;
import java.util.Optional;


public interface ProgressContentRepository extends JpaRepository<ProgressContent, Long> {
    List<ProgressContent> findByUser_UserId(Long UserId);
    List<ProgressContent> findByProgressId(Long progressId);
    Optional<ProgressContent> findByProgressIdAndUser_UserId(Long progressId, Long UserId);
}
