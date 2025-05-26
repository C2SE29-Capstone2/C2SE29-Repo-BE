package org.example.systemeduai.dto.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherUpdateDto {
    @NotBlank(message = "Teacher name cannot be blank")
    private String teacherName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(086|096|097|098|038|037|036|035|034|033|032|091|094|088|081|082|083|084|085|070|076|077|078|079|089|090|093|092|052|056|058|099|059|087)\\d{7}$",
            message = "Phone numbers are only allowed to have 10 digits and the prefix of the Vietnamese network operator")
    private String teacherPhone;
    @NotNull(message = "Student gender cannot be null")
    private Boolean teacherGender;

    @NotNull(message = "Date of birth cannot be null")
    @Past(message = "Date of birth must be in the past")
    private Date dateOfBirth;

    @NotBlank(message = "Student address cannot be blank")
    private String teacherAddress;

    private String profileImage;

    @NotBlank(message = "Qualifications cannot be blank")
    private String qualifications;

    @NotBlank(message = "Emails cannot be blank")
    @Email(message = "Invalid email format")
    private String accountEmail;
}
