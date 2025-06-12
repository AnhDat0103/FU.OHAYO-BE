package vn.fu_ohayo.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.response.NotificationDTO;
import vn.fu_ohayo.entity.Notification;
import vn.fu_ohayo.enums.NotificationType;
import vn.fu_ohayo.repository.NotificationRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.NotificationService;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private  final UserRepository userRepository;
    private  final NotificationMapper notificationMapper;
    @Override
    public void confirmNotification(Long notificationId) {
        logger.info("Confirm notification with ID: {}", notificationId);
        Optional<Notification> optional = notificationRepository.findById(notificationId);
        if (optional.isPresent()) {
            Notification notification = optional.get();
            notification.setStatus(true);
            notificationRepository.save(notification);
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
            notification.setStatus(false);
            notificationRepository.save(notification);
            messagingTemplate.convertAndSend("/topic/user-" + notification.getUser().getUserId(), notification);
        } else {
            logger.error("Notification with ID {} not found", notificationId);
            throw new RuntimeException("Notification not found");
        }
    }

    @Override
    public Notification notifyUser(Notification notification) {
        logger.info("Sending notification to user: {}", notification.getUser().getUserId());
        Notification savedNotification = notificationRepository.save(notification);
        if (notification.getType() == NotificationType.CONFIRMATION) {
            logger.info("Notification type: CONFIRMATION");
            messagingTemplate.convertAndSend("/topic/confirmation-user-" + notification.getUser().getUserId(), notification);
        } else if (notification.getType() == NotificationType.PAYMENT) {
            logger.info("Notification type: PAYMENT");
            messagingTemplate.convertAndSend("/topic/payment-user-" + notification.getUser().getUserId(), notification);
        } else {
            logger.warn("Unknown notification type: {}", notification.getType());
        }
        return savedNotification;
    }
    // In NotificationServiceImpl.java

    @Override
    public void handleNotificationAction(Long notificationId, boolean isConfirmed) {
        Optional<Notification> optional = notificationRepository.findById(notificationId);
        if (optional.isPresent()) {
            Notification notification = optional.get();
            if (notification.getType() == NotificationType.CONFIRMATION) {
                // Confirmation logic
                notification.setStatus(isConfirmed ? "confirmed" : "denied");
                notificationRepository.save(notification);
                messagingTemplate.convertAndSend("/topic/confirmation-user-" + notification.getUser().getUserId(), notification);
            } else if (notification.getType() == NotificationType.PAYMENT) {
                // Payment logic
                notification.setStatus(isConfirmed ? "paid" : "denied");
                notificationRepository.save(notification);
                messagingTemplate.convertAndSend("/topic/payment-user-" + notification.getUser().getUserId(), notification);
            } else {
                logger.warn("Unknown notification type: {}", notification.getType());
            }
        } else {
            logger.error("Notification with ID {} not found", notificationId);
            throw new RuntimeException("Notification not found");
        }
    }
}
