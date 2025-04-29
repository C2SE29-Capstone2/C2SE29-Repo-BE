package org.example.systemeduai.service;

import org.example.systemeduai.model.Account;

public interface IAccountService {
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Account save(Account account);

    Account findByEmail(String email);

    Account findByUsername(String username);

    String generateCode();

    void sendEmailOTP(String email, String code);

    void storeOtp(String username, String otp);

    boolean verifyOtp(String username, String otp);

    void clearOtp(String username);

    void changePasswordByForgot(String password, Account account);
}
