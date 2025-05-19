package org.example.systemeduai.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.systemeduai.model.enums.MealTime;

import javax.persistence.*;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class NutritionMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nutritionMenuId;

    @Enumerated(EnumType.STRING)
    private MealTime mealTime;

    private String mealDetails;
    private String healthCondition;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;
}
