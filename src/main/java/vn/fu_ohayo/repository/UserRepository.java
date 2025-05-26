package vn.fu_ohayo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.fu_ohayo.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByFullName(String fullName);
    Optional<User> findByEmailOrFullName(String email, String fullName);
    boolean existsByEmail(String email);
    boolean existsByFullName(String fullName);
}