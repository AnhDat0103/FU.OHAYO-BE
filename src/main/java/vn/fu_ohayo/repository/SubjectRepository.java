package vn.fu_ohayo.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.dto.response.SubjectResponse;
import vn.fu_ohayo.entity.Subject;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    @Query("SELECT s FROM Subject s WHERE s.subjectName LIKE %?1%")
    List<Subject> findBySubjectNameLike(String name);

    Subject findBySubjectCode(String subjectCode);

    boolean existsBySubjectCode(String subjectCode);

    @Query("""
    SELECT s.subjectId, s.subjectName, s.subjectCode, s.description,
           COUNT(DISTINCT su.userId), COUNT(DISTINCT l.lessonId)
    FROM Subject s
    LEFT JOIN s.users su
    LEFT JOIN s.lessons l
    WHERE s.subjectId = :subjectId
    GROUP BY s.subjectId, s.subjectName, s.subjectCode, s.description
""")
    Object[] getSubjectDetail(@Param("subjectId") int subjectId);

    @Query("""
    SELECT new vn.fu_ohayo.dto.response.SubjectResponse(
        s.subjectId, s.subjectCode, s.subjectName, s.description, s.status,
        COUNT(DISTINCT su.userId), s.updatedAt
    ) FROM Subject s LEFT JOIN s.users su
    GROUP BY s.subjectId, s.subjectCode, s.subjectName, s.description, s.status, s.updatedAt
""")
    List<SubjectResponse> findAllSubjectWithUserCount();

    boolean existsBySubjectName(String subjectName);

    @Query("SELECT COUNT(DISTINCT su.userId) FROM Subject s " +
            "LEFT JOIN s.users su" +
            " WHERE s.subjectId = :subjectId"
    )
    int countUsersBySubjectId(@Param("subjectId") int subjectId);

}
