package org.example.systemeduai.repository;

import org.example.systemeduai.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IStudentRepository extends JpaRepository<Student, Integer> {

    boolean existsByStudentPhone(String phone);

    @Query("SELECT s FROM Student s WHERE s.account.accountId = :accountId")
    Student findByAccountId(@Param("accountId") Integer accountId);
}
