package org.example.systemeduai.repository;

import org.example.systemeduai.model.ClassroomTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IClassroomTeacherRepository extends JpaRepository<ClassroomTeacher, Integer> {
}
