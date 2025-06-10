package vn.fu_ohayo.repository;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.fu_ohayo.entity.ProgressContent;
import java.util.List;


public interface ProgressContentRepository extends JpaRepository<ProgressContent, Long> {
    List<ProgressContent> findByUser_UserId(Long userUserId);
    List<ProgressContent> findByProgressId(Long progressId);
}
