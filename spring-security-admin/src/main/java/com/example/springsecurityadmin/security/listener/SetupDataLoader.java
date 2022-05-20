package com.example.springsecurityadmin.security.listener;

import com.example.springsecurityadmin.domain.entity.Account;
import com.example.springsecurityadmin.domain.entity.Resources;
import com.example.springsecurityadmin.domain.entity.Role;
import com.example.springsecurityadmin.repository.ResourcesRepository;
import com.example.springsecurityadmin.repository.RoleRepository;
import com.example.springsecurityadmin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ResourcesRepository resourcesRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        setupSecurityResources();

        alreadySetup = true;
    }

    private void setupSecurityResources() {
        Role userRole = createRoleIfNotFound("ROLE_USER", "유저");
        Role managerRole = createRoleIfNotFound("ROLE_MANAGER", "매니저");
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");

        Set<Role> admin = new HashSet<>();
        admin.add(adminRole);

        Set<Role> user = new HashSet<>();
        user.add(userRole);

        Set<Role> manager = new HashSet<>();
        manager.add(managerRole);

        createUserIfNotFound("user", "user@user.com", "1234", user);
        createUserIfNotFound("admin", "admin@admin.com", "1234", admin);
        createUserIfNotFound("manager", "manager@manager.com", "1234", manager);

        createResourceIfNotFound("/admin/**", "", admin, "url", 1);
        createResourceIfNotFound("/mypage", "", user, "url", 2);
        createResourceIfNotFound("/messages", "", manager, "url", 3);
        createResourceIfNotFound("/config", "", manager, "url", 4);

    }

    @Transactional
    public Role createRoleIfNotFound(String roleName, String roleDesc) {

        Role role = roleRepository.findByRoleName(roleName);

        if (role == null) {
            role = Role.builder()
                    .roleName(roleName)
                    .roleDesc(roleDesc)
                    .build();
        }
        return roleRepository.save(role);
    }

    @Transactional
    public Account createUserIfNotFound(final String userName, final String email, final String password, Set<Role> roleSet) {

        Account account = userRepository.findByUsername(userName)
                .orElse(Account.builder()
                        .username(userName)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .userRoles(roleSet)
                        .build());

        return userRepository.save(account);
    }

    @Transactional
    public Resources createResourceIfNotFound(String resourceName, String httpMethod, Set<Role> roleSet, String resourceType, int orderNum) {
        Resources resources = resourcesRepository.findByResourceNameAndHttpMethod(resourceName, httpMethod);

        if (resources == null) {
            resources = Resources.builder()
                    .resourceName(resourceName)
                    .roleSet(roleSet)
                    .httpMethod(httpMethod)
                    .resourceType(resourceType)
                    .orderNum(orderNum)
                    .build();
        }
        return resourcesRepository.save(resources);
    }
}