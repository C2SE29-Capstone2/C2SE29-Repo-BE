package org.example.systemeduai.service.impl;

import org.example.systemeduai.model.Account;
import org.example.systemeduai.repository.IAccountRepository;
import org.example.systemeduai.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private IAccountRepository accountRepository;

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
}
