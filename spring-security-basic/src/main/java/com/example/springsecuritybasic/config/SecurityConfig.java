package com.example.springsecuritybasic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

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

        http
                .rememberMe()
                .rememberMeParameter("remember-me") // 파라미터 name 변경 (기본 값은 remember-me)
                .tokenValiditySeconds(3600)  // 만료 기간 (기본 값 14일)
                .userDetailsService(userDetailsService)  // remember me 인증 시 유저계정 조회 시 필요한 클래스
                ;

    }
}
