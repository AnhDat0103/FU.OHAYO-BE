package vn.fu_ohayo.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.response.NotificationDTO;
import vn.fu_ohayo.entity.Notification;
import vn.fu_ohayo.entity.ParentStudent;
import vn.fu_ohayo.enums.NotificationEnum;
import vn.fu_ohayo.enums.ParentCodeStatus;
import vn.fu_ohayo.mapper.NotificationMapper;
import vn.fu_ohayo.repository.NotificationRepository;
import vn.fu_ohayo.repository.ParentStudentRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.NotificationService;

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
    private final ParentStudentRepository parentStudentRepository;
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
        if (notification.getType() == NotificationEnum.ACCEPT_STUDENT) {
            logger.info("Notification type: CONFIRMATION");
        } else if (notification.getType() == NotificationEnum.PAYMENT) {
            logger.info("Notification type: PAYMENT");
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
            notification.setStatusSend(true);
            if (notification.getType() == NotificationEnum.ACCEPT_STUDENT) {
                ParentStudent parentStudent = parentStudentRepository.findByParentEmailAndStudentEmailAndParentCodeStatus(notification.getUser().getEmail(), notification.getUserSend().getEmail(), ParentCodeStatus.PENDING);
                if (isConfirmed) {
                    parentStudent.setParentCodeStatus(ParentCodeStatus.CONFIRM);
                    notification.setStatus(true);
                } else {
                    parentStudent.setParentCodeStatus(ParentCodeStatus.REJECT);
                    notification.setStatus(false);
                }
                parentStudentRepository.save(parentStudent);
                notificationRepository.save(notification);

            } else {
                if (isConfirmed) {
                    // Payment logic
                    notification.setStatus(true);
                    notificationRepository.save(notification);
                } else {
                    // Payment logic
                    notification.setStatus(false);
                    notificationRepository.save(notification);
                }
            }


        } else {
            logger.error("Notification with ID {} not found", notificationId);
            throw new RuntimeException("Notification not found");
        }
    }

    @Override
    public List<NotificationDTO> getNotificationList(Long userId) {
        List<Notification> notifications = notificationRepository.findByUser_UserId(userId);
        return notifications.stream().map(notificationMapper::notificationDTO).toList();
    }
}
