package vn.fu_ohayo.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.entity.Notification;
import vn.fu_ohayo.repository.NotificationRepository;
import vn.fu_ohayo.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Override
    public void confirmNotification(Long notificationId) {
        //search for the notification by ID
        logger.info("Confirm notification with ID: {}", notificationId);
        Optional<Notification> optional = notificationRepository.findById(notificationId);
        if (optional.isPresent()) {
            Notification notification = optional.get();
            notification.setStatus("confirmed");
            notificationRepository.save(notification);
            // Send the updated notification to the user via WebSocket
            messagingTemplate.convertAndSend("/topic/user-" + notification.getUser().getUserId(), notification);
        } else {
            logger.error("Notification with ID {} not found", notificationId);
            throw new RuntimeException("Notification not found");
        }
    }
    @Override
    public void denyPayment(Long notificationId) {
        logger.info("Deny payment for notification with ID: {}", notificationId);
        Optional<Notification> optional = notificationRepository.findById(notificationId);
        if (optional.isPresent()) {
            Notification notification = optional.get();
            notification.setStatus("denied");
            notificationRepository.save(notification);
            messagingTemplate.convertAndSend("/topic/user-" + notification.getUser().getUserId(), notification);
        } else {
            logger.error("Notification with ID {} not found", notificationId);
            throw new RuntimeException("Notification not found");
        }
    }
}
