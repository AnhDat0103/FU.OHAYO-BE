package vn.fu_ohayo.service;

import java.util.List;
import vn.fu_ohayo.entity.Notification;

public interface NotificationService {
    void confirmNotification(Long notificationId);
    void denyPayment(Long notificationId);
    Notification notifyUser(Notification notification);
    void handleNotificationAction(Long notificationId, boolean isConfirmed);
}
