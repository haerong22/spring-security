package com.example.springsecurityadmin.controller.admin;

import com.example.springsecurityadmin.domain.dto.ResourcesDto;
import com.example.springsecurityadmin.domain.entity.Resources;
import com.example.springsecurityadmin.domain.entity.Role;
import com.example.springsecurityadmin.repository.RoleRepository;
import com.example.springsecurityadmin.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import com.example.springsecurityadmin.service.MethodSecurityService;
import com.example.springsecurityadmin.service.ResourcesService;
import com.example.springsecurityadmin.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ResourcesController {

    private final ResourcesService resourcesService;
    private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final UrlFilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;
    private final MethodSecurityService methodSecurityService;

    @GetMapping(value="/admin/resources")
    public String getResources(Model model) throws Exception {

        List<Resources> resources = resourcesService.getResources();
        model.addAttribute("resources", resources);

        return "admin/resource/list";
    }

    @GetMapping(value="/admin/resources/{id}")
    public String getResources(@PathVariable long id, Model model) throws Exception {

        List<Role> roleList = roleService.getRoles();
        model.addAttribute("roleList", roleList);
        Resources resources = resourcesService.getResources(id);

        ModelMapper modelMapper = new ModelMapper();
        ResourcesDto resourcesDto = modelMapper.map(resources, ResourcesDto.class);
        model.addAttribute("resources", resourcesDto);

        return "admin/resource/detail";
    }

    @GetMapping(value="/admin/resources/register")
    public String viewRoles(Model model) throws Exception {

        List<Role> roleList = roleService.getRoles();
        model.addAttribute("roleList", roleList);

        ResourcesDto resources = new ResourcesDto();
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role());
        resources.setRoleSet(roleSet);
        model.addAttribute("resources", resources);

        return "admin/resource/detail";
    }

    @PostMapping(value="/admin/resources")
    public String createResources(ResourcesDto resourcesDto) throws Exception {

        ModelMapper modelMapper = new ModelMapper();
        Role role = roleRepository.findByRoleName(resourcesDto.getRoleName());
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        Resources resources = modelMapper.map(resourcesDto, Resources.class);
        resources.setRoleSet(roles);

        resourcesService.createResources(resources);

        if ("url".equals(resourcesDto.getResourceType())) {
            filterInvocationSecurityMetadataSource.reload();
        } else {
            methodSecurityService.addMethodSecured(resourcesDto.getResourceName(),resourcesDto.getRoleName());
        }


        return "redirect:/admin/resources";
    }

    @GetMapping(value="/admin/resources/delete/{id}")
    public String removeResources(@PathVariable String id, Model model) throws Exception {

        Resources resources = resourcesService.getResources(Long.parseLong(id));
        resourcesService.deleteResources(Long.parseLong(id));
        filterInvocationSecurityMetadataSource.reload();

        return "redirect:/admin/resources";
    }
}
