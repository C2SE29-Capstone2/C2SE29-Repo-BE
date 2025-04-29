package org.example.systemeduai.service.impl;

import org.example.systemeduai.model.Account;
import org.example.systemeduai.repository.IAccountRepository;
import org.example.systemeduai.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Map<String, String> otpStorage = new HashMap<>();

    @Override
    public Boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public Account findByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }

    @Override
    public String generateCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    @Override
    public void sendEmailOTP(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email lấy lại mật khẩu từ EduAI");
        message.setText("Chào bạn!\n"
                + "EduAI gửi bạn mã OTP bên dưới để đổi lại mật khẩu.\n"
                + "Mã OTP: " + code + "\n\n"
                + "Vui lòng sử dụng mã này trong vòng 5 phút.\n"
                + "Thanks and regards!");
        emailSender.send(message);
    }

    @Override
    public void storeOtp(String username, String otp) {
        otpStorage.put(username, otp);
    }

    @Override
    public boolean verifyOtp(String username, String otp) {
        String storedOtp = otpStorage.get(username);
        return storedOtp != null && storedOtp.equals(otp);
    }

    @Override
    public void clearOtp(String username) {
        otpStorage.remove(username);
    }

    @Override
    public void changePasswordByForgot(String password, Account account) {
        accountRepository.changePassword(passwordEncoder.encode(password), account.getUsername());
    }
}
