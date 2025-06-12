package vn.fu_ohayo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Subject;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.Provider;
import vn.fu_ohayo.enums.SubjectStatus;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByFullName(String fullName);
    boolean existsByEmail(String email);
    boolean existsByEmailAndProvider(String email, Provider provider);
    Optional<User> findByEmailAndProvider(String email, Provider provider);
    boolean existsByPhone(String phone);


//    @Query(
//            "SELECT u FROM User u JOIN u.subjects s WHERE s.subjectId = :courseId  AND u.userId = :userId"
//    )
//    User findBySubjectId(int courseId, int userId);
}
