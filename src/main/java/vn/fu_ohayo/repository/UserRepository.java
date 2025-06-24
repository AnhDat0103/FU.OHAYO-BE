package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.enums.Provider;
import vn.fu_ohayo.enums.UserStatus;

import java.util.Date;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByFullName(String fullName);
    boolean existsByEmail(String email);
    boolean existsByEmailAndProvider(String email, Provider provider);
    Optional<User> findByEmailAndProvider(String email, Provider provider);
    boolean existsByPhone(String phone);
    int countAllByStatus(UserStatus status);
    int countAllByStatusAndCreatedAtBefore(UserStatus status, Date createdAt);
    int countAllByStatusAndMembershipLevel(UserStatus UserStatus, MembershipLevel membershipLevel);

//    @Query(
//            "SELECT u FROM User u JOIN u.subjects s WHERE s.subjectId = :courseId  AND u.userId = :userId"
//    )
//    User findBySubjectId(int courseId, int userId);
}
