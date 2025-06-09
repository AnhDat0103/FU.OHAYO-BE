package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.ProgressSubject;
import vn.fu_ohayo.entity.Subject;
import vn.fu_ohayo.entity.User;

@Repository
public interface ProgressSubjectRepository extends JpaRepository<ProgressSubject, Integer> {
    ProgressSubject findBySubjectAndUser(Subject subject, User user);
}
