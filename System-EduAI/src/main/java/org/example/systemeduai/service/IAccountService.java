package org.example.systemeduai.service;

import org.example.systemeduai.model.Account;

public interface IAccountService {
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Account save(Account account);

    Account findByEmail(String email);
}
