package com.example.springsecurityadmin.service.impl;

import com.example.springsecurityadmin.domain.dto.AccountDto;
import com.example.springsecurityadmin.domain.entity.Account;
import com.example.springsecurityadmin.repository.UserRepository;
import com.example.springsecurityadmin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public AccountDto getUser(Long id) {

        Account account = userRepository.findById(id).orElse(new Account());
        ModelMapper modelMapper = new ModelMapper();
        AccountDto accountDto = modelMapper.map(account, AccountDto.class);

        List<String> roles = account.getUserRoles()
                .stream()
                .map(role -> role.getRoleName())
                .collect(Collectors.toList());

        accountDto.setRoles(roles);
        return accountDto;
    }

    @Override
    @Transactional
    public List<Account> getUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void createUser(Account account) {
        userRepository.save(account);
    }
}
