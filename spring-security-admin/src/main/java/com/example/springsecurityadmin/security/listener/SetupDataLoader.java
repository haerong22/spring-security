package com.example.springsecurityadmin.security.listener;

import com.example.springsecurityadmin.domain.entity.*;
import com.example.springsecurityadmin.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ResourcesRepository resourcesRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleHierarchyRepository roleHierarchyRepository;
    private final AccessIpRepository accessIpRepository;

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        setupSecurityResources();
        setupAccessIpData();

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

        createRoleHierarchyIfNotFound(managerRole, adminRole);
        createRoleHierarchyIfNotFound(userRole, managerRole);

        createUserIfNotFound("user", "user@user.com", "1234", user);
        createUserIfNotFound("admin", "admin@admin.com", "1234", admin);
        createUserIfNotFound("manager", "manager@manager.com", "1234", manager);

        createResourceIfNotFound("/admin/**", "", admin, "url", 1);
        createResourceIfNotFound("/mypage", "", user, "url", 2);
        createResourceIfNotFound("/messages", "", manager, "url", 3);
        createResourceIfNotFound("/config", "", manager, "url", 4);
        createResourceIfNotFound("com.example.springsecurityadmin.aopsecurity.AopMethodService.methodSecured", "", manager, "method", 5);
        createResourceIfNotFound("execution(* com.example.springsecurityadmin.aopsecurity.AopPointcutService.pointcut*(..))", "", manager, "pointcut", 6);
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

    @Transactional
    public void createRoleHierarchyIfNotFound(Role childRole, Role parentRole) {

        RoleHierarchy roleHierarchy = roleHierarchyRepository.findByChildName(parentRole.getRoleName());
        if (roleHierarchy == null) {
            roleHierarchy = RoleHierarchy.builder()
                    .childName(parentRole.getRoleName())
                    .build();
        }
        RoleHierarchy parentRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);

        roleHierarchy = roleHierarchyRepository.findByChildName(childRole.getRoleName());
        if (roleHierarchy == null) {
            roleHierarchy = RoleHierarchy.builder()
                    .childName(childRole.getRoleName())
                    .build();
        }

        RoleHierarchy childRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);
        childRoleHierarchy.setParentName(parentRoleHierarchy);
    }

    private void setupAccessIpData() {
        AccessIp byIpAddress = accessIpRepository.findByIpAddress("127.0.0.1");
        if (byIpAddress == null) {
            AccessIp accessIp = AccessIp.builder()
                    .ipAddress("127.0.0.1")
                    .build();

            accessIpRepository.save(accessIp);

            accessIp = AccessIp.builder()
                    .ipAddress("0:0:0:0:0:0:0:1")
                    .build();

            accessIpRepository.save(accessIp);
        }

    }
}