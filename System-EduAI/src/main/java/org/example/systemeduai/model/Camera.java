package org.example.systemeduai.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class Camera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cameraId;
    private String location;
    private String ipAddress;
    private Boolean cameraStatus;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;
}
