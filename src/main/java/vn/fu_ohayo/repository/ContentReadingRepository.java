package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Content;
import vn.fu_ohayo.entity.ContentReading;

@Repository
public interface ContentReadingRepository extends JpaRepository<ContentReading, Long> {
    ContentReading findByContent(Content content);
}
