package com.example.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.AccountRepository;
import com.example.entity.Account;

@Service
public class AccountService {
    AccountRepository accountRepository;
    
    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Optional<Account> addAccount(Account account){
        return Optional.of(accountRepository.save(account));
    }

    public Optional<Account> getAccount(Account account){
        String username = account.getUsername();
        String password = account.getPassword();
        Optional<Account> accountOptional = accountRepository.findAccountByUsernameAndPassword(username, password);
        return accountOptional;
    }

    public Optional<Account> findAccountById(int account_id){
        Optional<Account> accountOptional = accountRepository.findById(account_id);
        return accountOptional;
    }
}
