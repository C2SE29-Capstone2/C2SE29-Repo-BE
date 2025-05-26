package org.example.systemeduai.repository;

import org.example.systemeduai.model.ClassroomTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface IClassroomTeacherRepository extends JpaRepository<ClassroomTeacher, Integer> {
    @Query(value = "SELECT * FROM classroom_teachers WHERE teacher_id = :teacherId AND role = :role", nativeQuery = true)
    List<ClassroomTeacher> findByTeacher_TeacherIdAndRole(@Param("teacherId") Integer teacherId, @Param("role") String role);

    List<ClassroomTeacher> findByTeacherTeacherId(Integer teacherId);

    boolean existsByTeacherTeacherIdAndClassroomClassroomId(Integer teacherId, Integer classroomId);

}
