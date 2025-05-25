package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Dialogue;

@Repository
public interface DialogueRepository extends JpaRepository<Dialogue, Long> {
}
