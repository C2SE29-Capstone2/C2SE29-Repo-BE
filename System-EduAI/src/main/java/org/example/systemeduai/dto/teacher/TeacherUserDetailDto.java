package org.example.systemeduai.dto.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Tuple;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherUserDetailDto {
    Integer teacherId;
    String teacherName;
    String teacherPhone;
    Boolean teacherGender;
    Date dateOfBirth;
    String teacherAddress;
    String profileImage;
    String qualifications;
    String username;
    String accountEmail;

    public static TeacherUserDetailDto TupleToTeacherDto(Tuple tuple) {
        return new TeacherUserDetailDto(
                tuple.get("teacher_id", Integer.class),
                tuple.get("teacher_name", String.class),
                tuple.get("teacher_phone", String.class),
                tuple.get("teacher_gender", Boolean.class),
                tuple.get("date_of_birth", Date.class),
                tuple.get("teacher_address", String.class),
                tuple.get("profile_image", String.class),
                tuple.get("qualifications", String.class),
                tuple.get("username", String.class),
                tuple.get("email", String.class)
        );
    }
}
