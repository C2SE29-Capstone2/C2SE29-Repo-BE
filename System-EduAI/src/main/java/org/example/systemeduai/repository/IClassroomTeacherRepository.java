package org.example.systemeduai.repository;

import org.example.systemeduai.model.ClassroomTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface IClassroomTeacherRepository extends JpaRepository<ClassroomTeacher, Integer> {
    List<ClassroomTeacher> findByClassroomClassroomId(Integer classroomId);
    List<ClassroomTeacher> findByTeacherTeacherId(Integer teacherId);
    @Modifying
    @Transactional
    void deleteByTeacherTeacherId(Integer teacherId);
}
