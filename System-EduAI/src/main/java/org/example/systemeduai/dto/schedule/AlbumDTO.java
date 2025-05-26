package org.example.systemeduai.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDTO {
    private Integer albumId;
    private String albumName;
    private String description;
    private LocalDate createdDate;
    private String imageUrls;
    private Integer classroomId;
    private Integer activityId;
}
