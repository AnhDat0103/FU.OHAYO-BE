// src/main/java/vn/fu_ohayo/controller/NotificationController.java
package vn.fu_ohayo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@AllArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/{notificationId}/confirm")
    public void confirmPayment(@PathVariable("notificationId") Long notificationId) {
        notificationService.confirmNotification(notificationId);
    }

    @PostMapping("/{notificationId}/deny")
    public void denyPayment(@PathVariable("notificationId") Long notificationId) {
        notificationService.denyPayment(notificationId);
    }

//    @GetMapping("/{userId}/notificationUser")
//    public ResponseEntity<?> getAllNotificationsOfUser (@PathVariable("notificationId") Long notificationId) {
//
//
//    }
}