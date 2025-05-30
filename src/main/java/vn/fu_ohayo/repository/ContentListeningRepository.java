package vn.fu_ohayo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Content;
import vn.fu_ohayo.entity.ContentListening;
@Repository
public interface ContentListeningRepository extends JpaRepository<ContentListening, Long> {
    ContentListening findByContent(Content content);
    ContentListening findBycontentListeningId(long id);

}
