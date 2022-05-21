package com.example.springsecurityadmin.service.impl;

import com.example.springsecurityadmin.domain.entity.Resources;
import com.example.springsecurityadmin.repository.ResourcesRepository;
import com.example.springsecurityadmin.service.ResourcesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourcesServiceImpl implements ResourcesService {

    private final ResourcesRepository resourcesRepository;

    @Override
    @Transactional
    public Resources getResources(long id) {
        return resourcesRepository.findById(id).orElse(new Resources());
    }

    @Override
    public List<Resources> getResources() {
        return resourcesRepository.findAll(Sort.by(Sort.Order.asc("orderNum")));
    }

    @Override
    @Transactional
    public void createResources(Resources resources){
        resourcesRepository.save(resources);
    }

    @Override
    @Transactional
    public void deleteResources(long id) {
        resourcesRepository.deleteById(id);
    }
}
