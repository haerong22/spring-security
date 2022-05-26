package com.example.springsecurityadmin.security.config;

import com.example.springsecurityadmin.security.factory.MethodResourcesFactoryBean;
import com.example.springsecurityadmin.security.factory.PointcutResourcesFactoryBean;
import com.example.springsecurityadmin.security.proccessor.ProtectPointcutPostProcessor;
import com.example.springsecurityadmin.service.SecurityResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    private final MethodResourcesFactoryBean methodResourcesFactoryBean;
    private final PointcutResourcesFactoryBean pointcutResourcesFactoryBean;

    @Override
    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        return mapBasedMethodSecurityMetadataSource();
    }

    @Bean
    public MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource() {
        return new MapBasedMethodSecurityMetadataSource(methodResourcesFactoryBean.getObject());
    }

    @Bean
    public ProtectPointcutPostProcessor protectPointcutPostProcessor() {
        ProtectPointcutPostProcessor protectPointcutPostProcessor =
                new ProtectPointcutPostProcessor(mapBasedMethodSecurityMetadataSource());

        protectPointcutPostProcessor.setPointcutMap(pointcutResourcesFactoryBean.getObject());
        return protectPointcutPostProcessor;
    }

//    @Bean
//    BeanPostProcessor protectPointcutPostProcessor() throws Exception {
//
//        Class<?> clazz = Class.forName("org.springframework.security.config.method.ProtectPointcutPostProcessor");
//        Constructor<?> declaredConstructor = clazz.getDeclaredConstructor(MapBasedMethodSecurityMetadataSource.class);
//        declaredConstructor.setAccessible(true);
//        Object instance = declaredConstructor.newInstance(mapBasedMethodSecurityMetadataSource());
//        Method setPointcutMap = instance.getClass().getMethod("setPointcutMap", Map.class);
//        setPointcutMap.setAccessible(true);
//        setPointcutMap.invoke(instance, pointcutResourcesFactoryBean.getObject());
//
//        return (BeanPostProcessor) instance;
//    }

}
