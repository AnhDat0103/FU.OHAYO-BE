package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.ProgressSubject;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ProgressStatus;

import java.util.List;

@Repository
public interface ProgressSubjectRepository extends JpaRepository<ProgressSubject, Integer> {
    List<ProgressSubject> findAllByUserAndProgressStatus(User user, ProgressStatus progressStatus);
}
