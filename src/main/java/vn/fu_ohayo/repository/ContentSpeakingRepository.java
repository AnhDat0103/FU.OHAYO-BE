package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Content;
import vn.fu_ohayo.entity.ContentSpeaking;

@Repository
public interface ContentSpeakingRepository extends JpaRepository<ContentSpeaking,Long> {

    ContentSpeaking findByContent(Content content);
}
