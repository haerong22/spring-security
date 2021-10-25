package com.study.springsecurity.web.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true) // 권한에 따라 접근하도록 설정
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthDetails customAuthDetails;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 어드민은 유저 권한도 가지도록 설정
    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(User.builder()
                        .username("user1")
                        .password(passwordEncoder().encode("1234"))
                        .roles("USER"))
                .withUser(User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode("1234"))
                        .roles("ADMIN"));

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()

                // 로그인 성공시, 실패시
                .and()
                .formLogin().loginPage("/login").permitAll()
                .defaultSuccessUrl("/", false)
                .failureUrl("/login-error")
//                .authenticationDetailsSource(customAuthDetails) // 커스텀

                // 로그아웃 설정
                .and()
                .logout().logoutSuccessUrl("/")

                // 예외 발생시 설정
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied");
    }

    // 시큐리티가 적용될 대상 설정
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 정적 리소스는 시큐리티 적용 X
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
