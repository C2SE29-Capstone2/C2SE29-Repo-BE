package org.example.systemeduai.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.systemeduai.dto.request.CreateNotificationRequest;
import org.example.systemeduai.dto.request.CreateScheduledNotificationRequest;
import org.example.systemeduai.dto.response.NotificationResponse;
import org.example.systemeduai.enums.NotificationRecipient;
import org.example.systemeduai.exception.ResourceNotFoundException;
import org.example.systemeduai.model.Notification;
import org.example.systemeduai.repository.INotificationRepository;
import org.example.systemeduai.service.INotificationService;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {
    private final INotificationRepository notificationRepository;
    private final TaskScheduler taskScheduler;
    @Override
    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll()
                .stream()
                .map(this::mapToNotificationResponse)
                .toList();
    }

    @Override
    public List<NotificationResponse> getDeliveredNotifications() {
        return notificationRepository.findBySendAtBefore(LocalDateTime.now())
                .stream()
                .map(this::mapToNotificationResponse)
                .toList();
    }

    @Override
    public NotificationResponse getNotificationById(Integer notificationId) {
        return mapToNotificationResponse(
                notificationRepository.findById(notificationId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Notification", "Id", notificationId)));
    }

    @Override
    public List<NotificationResponse> getNotificationByRecipient(String recipient) {
        return notificationRepository.findByRecipient(NotificationRecipient.valueOf(recipient))
                .stream()
                .map(this::mapToNotificationResponse)
                .toList();
    }

    @Override
    public List<NotificationResponse> getUnDeliveredNotifications() {
        return notificationRepository.findBySendAtAfter(LocalDateTime.now())
                .stream()
                .map(this::mapToNotificationResponse)
                .toList();
    }

    @Override
    public void sendNotification(CreateNotificationRequest request) {
        Notification notification = Notification.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .recipient(NotificationRecipient.valueOf(request.getRecipient()))
                .sendAt(LocalDateTime.now())
                .isDelivered(true)
                .build();
        notificationRepository.save(notification);
    }

    @Override
    public void scheduleNotification(CreateScheduledNotificationRequest request) {
        Notification notification = Notification.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .recipient(NotificationRecipient.valueOf(request.getRecipient()))
                .sendAt(request.getScheduleTime())
                .isDelivered(false)
                .build();
        notificationRepository.save(notification);
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        if (notification.getSendAt().isAfter(startOfDay) && notification.getSendAt().isBefore(endOfDay)) {
            scheduleSendNotification(notification);
        }
    }

    @Override
    public void cancelNotification(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Notification", "Id", notificationId));
        if (notification.getSendAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot cancel a notification that has already been sent");
        }
        notificationRepository.delete(notification);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void sendScheduledNotifications() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        List<Notification> notificationsSendToday = notificationRepository
                .findBySendAtBeforeAndSendAtAfter(startOfDay, endOfDay);
        notificationsSendToday.forEach(this::scheduleSendNotification);
    }

    private void scheduleSendNotification(Notification notification){
        Runnable sendNotification = () -> {
            notification.setDelivered(true);
            notificationRepository.save(notification);
        };
        taskScheduler.schedule(sendNotification,
                               notification.getSendAt().atZone(ZoneId.of("Asia/Saigon")).toInstant());
    }

    private NotificationResponse mapToNotificationResponse(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .recipient(notification.getRecipient().name())
                .sendAt(notification.getSendAt())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
