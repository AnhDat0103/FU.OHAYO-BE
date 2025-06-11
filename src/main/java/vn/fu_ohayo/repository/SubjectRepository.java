package vn.fu_ohayo.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.dto.response.SubjectResponse;
import vn.fu_ohayo.entity.ProgressSubject;
import vn.fu_ohayo.entity.Subject;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.SubjectStatus;

import java.util.Collection;
import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    boolean existsBySubjectCode(String subjectCode);

    boolean existsBySubjectName(String subjectName);

    boolean existsBySubjectCodeAndSubjectIdNot(String subjectCode, int subjectId);

    boolean existsBySubjectNameAndSubjectIdNot(String subjectName, int subjectId);

    @Query(
            "SELECT s FROM Subject s LEFT JOIN ProgressSubject ps ON s.subjectId = ps.subject.subjectId\n" +
                    "    AND ps.user.userId = :userId AND s.status = :status\n" +
                    "WHERE ps.subject.subjectId IS NULL "
    )
    Page<Subject> findAllByStatusAndProgressSubjectsIsEmpty(SubjectStatus status,long userId ,Pageable pageable);



}
