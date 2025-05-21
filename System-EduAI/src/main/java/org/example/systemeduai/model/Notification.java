package org.example.systemeduai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.systemeduai.enums.NotificationRecipient;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationId;
    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private NotificationRecipient recipient;
    private boolean isDelivered;
    private LocalDateTime sendAt;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
