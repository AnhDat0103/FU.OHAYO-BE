package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fu_ohayo.entity.ParentStudent;

import java.util.Optional;

public interface ParentStudentRepository extends JpaRepository<ParentStudent, Integer>
{
    ParentStudent findByVerificationCode(String code);
    boolean existsByVerificationCode(String verificationCode);
    Optional<ParentStudent> findByVerificationCodeAndStudentEmail(String verificationCode, String email);

}
