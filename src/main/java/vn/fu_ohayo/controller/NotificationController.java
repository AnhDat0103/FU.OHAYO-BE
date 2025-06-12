package vn.fu_ohayo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.entity.Notification;
import vn.fu_ohayo.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/{notificationId}/confirm")
    public ResponseEntity<String> confirmNotification(@PathVariable Long notificationId) {
        notificationService.handleNotificationAction(notificationId, true);
        return ResponseEntity.ok("Notification confirmed");
    }

    @PostMapping("/{notificationId}/deny")
    public ResponseEntity<String> denyNotification(@PathVariable Long notificationId) {
        notificationService.handleNotificationAction(notificationId, false);
        return ResponseEntity.ok("Notification denied");
    }

    @PostMapping("/notify")
    public ResponseEntity<Notification> notifyUser(@RequestBody Notification notification) {
        Notification saved = notificationService.notifyUser(notification);
        return ResponseEntity.ok(saved);
    }
}