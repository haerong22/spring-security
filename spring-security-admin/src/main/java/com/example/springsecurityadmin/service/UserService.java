package com.example.springsecurityadmin.service;

import com.example.springsecurityadmin.domain.dto.AccountDto;
import com.example.springsecurityadmin.domain.entity.Account;

import java.util.List;

public interface UserService {

    AccountDto getUser(Long id);
    List<Account> getUsers();
    void createUser(Account account);

    void modifyUser(AccountDto accountDto);
}
