package org.example.systemeduai.service.impl;

import org.example.systemeduai.dto.teacher.TeacherUpdateDto;
import org.example.systemeduai.dto.teacher.TeacherUserDetailDto;
import org.example.systemeduai.model.Account;
import org.example.systemeduai.model.Teacher;
import org.example.systemeduai.repository.IAccountRepository;
import org.example.systemeduai.repository.ITeacherRepository;
import org.example.systemeduai.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements ITeacherService {

    @Autowired
    private ITeacherRepository teacherRepository;

    @Autowired
    private IAccountRepository accountRepository;

    @Override
    public TeacherUserDetailDto findUserDetailByUsername(String username) {
        Tuple tuple = teacherRepository.findUserDetailByUsername(username).orElse(null);
        if (tuple == null) {
            return null;
        }
        return TeacherUserDetailDto.TupleToTeacherDto(tuple);
    }

    @Override
    public TeacherUserDetailDto updateTeacherDetails(String username, TeacherUpdateDto updateDto) {
        Optional<Teacher> teacherOpt = teacherRepository.findByUsername(username);
        if (!teacherOpt.isPresent()) {
            return null;
        }

        Teacher teacher = teacherOpt.get();
        Account account = teacher.getAccount();

        teacher.setTeacherName(updateDto.getTeacherName());
        teacher.setTeacherEmail(updateDto.getAccountEmail());
        teacher.setTeacherPhone(updateDto.getTeacherPhone());
        teacher.setTeacherGender(updateDto.getTeacherGender());
        teacher.setDateOfBirth(updateDto.getDateOfBirth());
        teacher.setTeacherAddress(updateDto.getTeacherAddress());
        teacher.setProfileImage(updateDto.getProfileImage());
        teacher.setQualifications(updateDto.getQualifications());

        account.setEmail(updateDto.getAccountEmail());

        teacherRepository.save(teacher);
        accountRepository.save(account);

        return new TeacherUserDetailDto(
                teacher.getTeacherId(),
                teacher.getTeacherName(),
                teacher.getTeacherPhone(),
                teacher.getTeacherGender(),
                teacher.getDateOfBirth(),
                teacher.getTeacherAddress(),
                teacher.getProfileImage(),
                teacher.getQualifications(),
                account.getUsername(),
                account.getEmail()
        );
    }

    @Override
    public Teacher findTeacherByUsername(String username) {
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        if (accountOpt.isEmpty()) {
            throw new RuntimeException("Tài khoản không tìm thấy với tên đăng nhập: " + username);
        }

        Account account = accountOpt.get();
        Optional<Teacher> teacherOpt = teacherRepository.findAll().stream()
                .filter(teacher -> teacher.getAccount() != null && teacher.getAccount().getAccountId().equals(account.getAccountId()))
                .findFirst();

        return teacherOpt.orElseThrow(() -> new RuntimeException("Giáo viên không tìm thấy với tên đăng nhập: " + username));
    }
}
