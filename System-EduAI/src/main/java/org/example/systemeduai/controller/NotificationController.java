package org.example.systemeduai.controller;

import lombok.RequiredArgsConstructor;
import org.example.systemeduai.dto.request.CreateNotificationRequest;
import org.example.systemeduai.dto.request.CreateScheduledNotificationRequest;
import org.example.systemeduai.dto.response.NotificationResponse;
import org.example.systemeduai.service.INotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final INotificationService notificationService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        List<NotificationResponse> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/delivered")
    public ResponseEntity<List<NotificationResponse>> getDeliveredNotifications() {
        List<NotificationResponse> notifications = notificationService.getDeliveredNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/undelivered")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponse>> getUnDeliveredNotifications() {
        List<NotificationResponse> notifications = notificationService.getUnDeliveredNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Integer notificationId) {
        NotificationResponse notification = notificationService.getNotificationById(notificationId);
        return ResponseEntity.ok(notification);
    }

    @GetMapping("/recipient/{recipient}")
    public ResponseEntity<List<NotificationResponse>> getNotificationByRecipient(@PathVariable String recipient) {
        List<NotificationResponse> notifications = notificationService.getNotificationByRecipient(recipient);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> sendNotification(@Valid @RequestBody CreateNotificationRequest request) {
        notificationService.sendNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/schedule")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> scheduleNotification(@Valid @RequestBody CreateScheduledNotificationRequest request) {
        notificationService.scheduleNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cancelNotification(@PathVariable Integer notificationId) {
        notificationService.cancelNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
} 