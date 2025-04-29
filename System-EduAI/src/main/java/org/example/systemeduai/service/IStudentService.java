package org.example.systemeduai.service;

import org.example.systemeduai.model.Student;

public interface IStudentService {

    void save(Student student);

    boolean existsByStudentPhone(String phone);
}
