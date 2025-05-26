package org.example.systemeduai.dto.announcement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDTO {
    private Integer announcementId;
    private String title;
    private String content;
    private LocalDate sentDate;
    private Integer teacherId;
}
