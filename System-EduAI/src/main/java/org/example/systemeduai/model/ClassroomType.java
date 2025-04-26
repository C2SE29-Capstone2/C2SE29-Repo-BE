package org.example.systemeduai.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class ClassroomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer classroomTypeId;
    private String classroomTypeName;
}
