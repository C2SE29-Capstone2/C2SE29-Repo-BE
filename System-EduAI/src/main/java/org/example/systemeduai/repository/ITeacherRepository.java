package org.example.systemeduai.repository;

import org.example.systemeduai.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ITeacherRepository extends JpaRepository<Teacher, Integer> {
}
