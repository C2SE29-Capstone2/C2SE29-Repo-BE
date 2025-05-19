package org.example.systemeduai.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.systemeduai.model.enums.MealTime;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class NutritionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nutritionLogId;

    @Enumerated(EnumType.STRING)
    private MealTime mealTime;

    private String mealDetails;
    private LocalDateTime logTime;
    private boolean isCompleted;
    private String notes;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nutrition_menu_id")
    private NutritionMenu nutritionMenu;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;
}
