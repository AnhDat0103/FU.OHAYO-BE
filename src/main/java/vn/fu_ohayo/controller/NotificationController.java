package vn.fu_ohayo.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.response.NotificationDTO;
import vn.fu_ohayo.entity.Notification;
import vn.fu_ohayo.service.NotificationService;

import java.util.List;

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


//    @GetMapping("/{userId}/notificationUser")
//    public ResponseEntity<?> getAllNotificationsOfUser (@PathVariable("notificationId") Long notificationId) {
//
//
//    }

        @GetMapping("/{userId}/notificationUser")
        public ResponseEntity<?> getAllNotificationsOfUser (@PathVariable("userId") Long userId) {
            List<NotificationDTO> list = notificationService.getAllOfUser(userId);
            return ResponseEntity.ok().body(list);
        }
    }