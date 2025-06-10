package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fu_ohayo.entity.ParentStudent;

import java.util.Optional;

public interface ParentStudentRepository extends JpaRepository<ParentStudent, Integer>
{
    Optional<ParentStudent> findByVerificationCode(String code);
}
