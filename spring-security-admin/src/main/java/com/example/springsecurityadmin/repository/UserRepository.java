package com.example.springsecurityadmin.repository;

import com.example.springsecurityadmin.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {
}
