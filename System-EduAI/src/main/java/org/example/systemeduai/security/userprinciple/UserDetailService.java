package org.example.systemeduai.security.userprinciple;

import org.example.systemeduai.model.Account;
import org.example.systemeduai.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    IAccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.getAccountByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found->username or password" + username));
        return UserPrinciple.build(account);
    }
}
