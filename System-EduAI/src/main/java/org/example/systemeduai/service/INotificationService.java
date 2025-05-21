package org.example.systemeduai.service;

import org.example.systemeduai.dto.request.CreateNotificationRequest;
import org.example.systemeduai.dto.request.CreateScheduledNotificationRequest;
import org.example.systemeduai.dto.response.NotificationResponse;

import java.util.List;

public interface INotificationService {
    List<NotificationResponse> getAllNotifications();
    List<NotificationResponse> getDeliveredNotifications();
    NotificationResponse getNotificationById(Integer notificationId);
    List<NotificationResponse> getNotificationByRecipient(String recipient);
    List<NotificationResponse> getUnDeliveredNotifications();
    void sendNotification(CreateNotificationRequest request);
    void scheduleNotification(CreateScheduledNotificationRequest request);
    void cancelNotification(Integer notificationId);
}
