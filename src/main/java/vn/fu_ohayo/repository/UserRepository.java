package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
