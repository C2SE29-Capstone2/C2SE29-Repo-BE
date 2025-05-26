package org.example.systemeduai.repository;

import org.example.systemeduai.dto.student.StudentUserDetailDto;
import org.example.systemeduai.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.util.Optional;

@Repository
@Transactional
public interface IStudentRepository extends JpaRepository<Student, Integer> {

    boolean existsByStudentPhone(String phone);

    @Query("SELECT s FROM Student s WHERE s.account.accountId = :accountId")
    Student findByAccountId(@Param("accountId") Integer accountId);

    @Query(value = "SELECT s.student_id, s.student_name, s.student_gender, s.date_of_birth, s.age, s.student_address, " +
            "s.profile_image, s.health_status, s.hobby, cb.height, cb.weight, a.username, a.email " +
            "FROM student s " +
            "INNER JOIN account a ON s.account_id = a.account_id " +
            "LEFT JOIN contact_book cb ON cb.student_id = s.student_id " +
            "WHERE a.is_enable = true AND a.username = :username", nativeQuery = true)
    Optional<Tuple> findUserDetailByUsername(@Param("username") String username);

    @Query(value = "SELECT s.* FROM student s " +
            "INNER JOIN parent p ON s.student_id = p.student_id " +
            "WHERE p.parent_id = :parentId",
            nativeQuery = true)
    Student findByParentParentId(@Param("parentId") Integer parentId);
}
