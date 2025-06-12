package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fu_ohayo.entity.Notification;
import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser_UserId(long userId);
}
