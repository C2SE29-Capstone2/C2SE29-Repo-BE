package org.example.systemeduai.service.impl;

import org.example.systemeduai.model.Student;
import org.example.systemeduai.repository.IStudentRepository;
import org.example.systemeduai.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private IStudentRepository studentRepository;

    @Override
    public void save(Student student) {
        studentRepository.save(student);
    }

    @Override
    public boolean existsByStudentPhone(String phone) {
        return studentRepository.existsByStudentPhone(phone);
    }

    @Override
    public Student findByAccountId(Integer accountId) {
        return studentRepository.findByAccountId(accountId);
    }
}
