package com.example.springsecurityadmin.service;

import com.example.springsecurityadmin.domain.entity.Resources;

import java.util.List;

public interface ResourcesService {

    List<Resources> getResources();
    Resources getResources(long id);
    void createResources(Resources resources);
    void deleteResources(long id);
}
