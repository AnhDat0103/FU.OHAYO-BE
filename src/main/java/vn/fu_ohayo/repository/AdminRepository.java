package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Admin;
import java.util.Optional;
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    @EntityGraph(attributePaths = "roles")
    Optional<Admin> findByUsername(String username);
    boolean existsByUsername(String username);
}
