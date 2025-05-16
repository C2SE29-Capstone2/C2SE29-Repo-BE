package org.example.systemeduai.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.systemeduai.validation.AgeConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdateDto {
    @NotBlank(message = "Student name cannot be blank")
    private String studentName;

    @NotNull(message = "Student gender cannot be null")
    private Boolean studentGender;

    @NotNull(message = "Date of birth cannot be null")
    @AgeConstraint(message = "Age must be between 3 and 6 years old based on date of birth")
    private Date dateOfBirth;

    @NotBlank(message = "Student address cannot be blank")
    private String studentAddress;

    private String profileImage;

    @NotBlank(message = "Health status cannot be blank")
    private String healthStatus;

    @NotBlank(message = "Hobby cannot be blank")
    private String hobby;
}
