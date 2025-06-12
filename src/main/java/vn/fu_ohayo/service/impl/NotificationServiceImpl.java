package vn.fu_ohayo.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.response.NotificationDTO;
import vn.fu_ohayo.entity.Notification;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.NotificationMapper;
import vn.fu_ohayo.repository.NotificationRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
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
        //search for the notification by ID
        logger.info("Confirm notification with ID: {}", notificationId);
        Optional<Notification> optional = notificationRepository.findById(notificationId);
        if (optional.isPresent()) {
            Notification notification = optional.get();
            notification.setStatus(true);
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
            notification.setStatus(false);
            notificationRepository.save(notification);
            messagingTemplate.convertAndSend("/topic/user-" + notification.getUser().getUserId(), notification);
        } else {
            logger.error("Notification with ID {} not found", notificationId);
            throw new RuntimeException("Notification not found");
        }
    }

    @Override
    public List<NotificationDTO> getAllOfUser(Long userId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        return notificationRepository.findByUser_UserId(user.getUserId()).stream().map(notificationMapper::notificationDTO).toList();
    }
}
