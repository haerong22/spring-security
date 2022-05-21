package com.example.springsecurityadmin.security.init;

import com.example.springsecurityadmin.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import com.example.springsecurityadmin.service.RoleHierarchyService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityInitializer implements ApplicationRunner {

    private final RoleHierarchyService roleHierarchyService;
    private final UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;
    private final RoleHierarchyImpl roleHierarchy;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String allHierarchy = this.roleHierarchyService.findAllHierarchy();
        this.roleHierarchy.setHierarchy(allHierarchy);

        urlFilterInvocationSecurityMetadataSource.reload();
    }
}
