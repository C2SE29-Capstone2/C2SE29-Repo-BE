package org.example.systemeduai.repository;

import org.example.systemeduai.model.Classroom;
import org.example.systemeduai.model.ClassroomTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface IClassroomRepository extends JpaRepository<Classroom, Integer> {
}
