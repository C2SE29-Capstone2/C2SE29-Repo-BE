package org.example.systemeduai.repository;

import org.example.systemeduai.enums.NotificationRecipient;
import org.example.systemeduai.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface INotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByRecipient(NotificationRecipient recipient);
    List<Notification> findBySendAtBefore(LocalDateTime dateTime);
    List<Notification> findBySendAtAfter(LocalDateTime dateTime);
    List<Notification> findBySendAtBeforeAndSendAtAfter(LocalDateTime start, LocalDateTime end);
}
