package com.example.springsecuritybasic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated();

        http
                .formLogin() // 폼 로그인 사용
//                .loginPage("/loginPage")
                .defaultSuccessUrl("/") // 로그인 성공시 이동 url
                .failureUrl("/login")  // 로그인 실패시 이동 url
                .usernameParameter("userId")  // 폼의 username 파라미터 name 값 변경
                .passwordParameter("passwd")  // 폼의 password 파라미터 name 값 변경
                .loginProcessingUrl("/login_proc")  // 로그인 수행 url
                .successHandler((request, response, authentication) -> {  // 로그인 성공 후 처리
                    System.out.println("authentication = " + authentication.getName());
                    response.sendRedirect("/");
                })
                .failureHandler((request, response, exception) -> {  // 로그인 실패 후 처리
                    System.out.println("exception = " + exception.getMessage());
                    response.sendRedirect("/login");
                })
                .permitAll();

        // 로그아웃은 기본적으로 post 방식으로 처리 (변경 가능)
        http
                .logout()
                .logoutUrl("/logout")  // 로그아웃 url
                .logoutSuccessUrl("/login")  // 로그아웃 성공 시 이동 url
                .addLogoutHandler((request, response, authentication) -> { // 로그아웃 수행 처리
                    HttpSession session = request.getSession();
                    session.invalidate();
                })
                .logoutSuccessHandler((request, response, authentication) -> { // 로그아웃 성공 시 처리
                    response.sendRedirect("/login");
                })
                .deleteCookies("JSESSIONID", "remember-me")  // 쿠키 삭제
                ;

    }
}
