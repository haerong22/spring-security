package com.example.springsecurityadmin.service;

import com.example.springsecurityadmin.domain.entity.Role;

import java.util.List;

public interface RoleService {

    List<Role> getRoles();
    Role getRole(long id);
}
