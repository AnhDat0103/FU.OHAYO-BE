package vn.fu_ohayo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.ProgressSubject;
import vn.fu_ohayo.entity.Subject;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ProgressStatus;
import vn.fu_ohayo.enums.SubjectStatus;

@Repository
public interface ProgressSubjectRepository extends JpaRepository<ProgressSubject, Integer> {
    ProgressSubject findBySubjectAndUserAndProgressStatus(Subject subject, User user, ProgressStatus progressStatus);

    int countUserBySubject_SubjectId(int subjectId);

    @Query(
            "SELECT ps.subject FROM ProgressSubject ps WHERE ps.user = :user AND ps.subject.status = :subjectStatus"
    )
    Page<Subject> findAllSubjectsByUserAndSubject_Status(User user, SubjectStatus subjectStatus, Pageable pageable);
}
