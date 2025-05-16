package org.example.systemeduai.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Tuple;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentUserDetailDto {
    Integer studentId;
    String studentName;
    Boolean studentGender;
    Date dateOfBirth;
    Integer age;
    String studentAddress;
    String profileImage;
    String healthStatus;
    String hobby;
    Double height;
    Double weight;
    String username;
    String accountEmail;

    public static StudentUserDetailDto TupleToStudentDto(Tuple tuple) {
        return new StudentUserDetailDto(
                tuple.get("student_id", Integer.class),
                tuple.get("student_name", String.class),
                tuple.get("student_gender", Boolean.class),
                tuple.get("date_of_birth", Date.class),
                tuple.get("age", Integer.class),
                tuple.get("student_address", String.class),
                tuple.get("profile_image", String.class),
                tuple.get("health_status", String.class),
                tuple.get("hobby", String.class),
                tuple.get("height") != null ? tuple.get("height", Double.class) : null,
                tuple.get("weight") != null ? tuple.get("weight", Double.class) : null,
                tuple.get("username", String.class),
                tuple.get("email", String.class)

        );
    }
}
