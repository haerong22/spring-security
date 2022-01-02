package com.example.springsecurityadmin.service.impl;

import com.example.springsecurityadmin.domain.Account;
import com.example.springsecurityadmin.repository.UserRepository;
import com.example.springsecurityadmin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createUser(Account account) {
        userRepository.save(account);
    }
}
