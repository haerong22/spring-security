package com.example.springsecuritybasic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
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

        // 폼 로그인
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

        // 로그아웃
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

        // Remember me 쿠키
        http
                .rememberMe()
                .rememberMeParameter("remember-me") // 파라미터 name 변경 (기본 값은 remember-me)
                .tokenValiditySeconds(3600)  // 만료 기간 (기본 값 14일)
                .userDetailsService(userDetailsService)  // remember me 인증 시 유저계정 조회 시 필요한 클래스
                ;

        // 동시 세션 제어
        http
                .sessionManagement()  // 세션 관리 기능
                .maximumSessions(1)  // 최대 허용 가능 세션 수, -1 : 무제한 로그인 세션 허용
                .maxSessionsPreventsLogin(true)  // true : 동시 로그인 차단, false : 기존 세션 만료(기본값)
                .expiredUrl("/expired")  // 세션 만료시 이동 페이지
                ;

        // 세션 고정 보호
        http
                .sessionManagement()
                .invalidSessionUrl("/invalid")  // 세션이 유효하지 않을 때 이동 페이지
                .sessionFixation().changeSessionId()  // 로그인 시 세션 아이디 새로 생성, 세션 내용은 변경되지 않는다. (기본값)
//                .sessionFixation().none()  // 로그인 시 세션 아이디 고정
//                .sessionFixation().migrateSession()  // 로그인 시 세션 아이디 새로 생성, 세션 내용은 변경되지 않는다.
//                .sessionFixation().newSession()  // 로그인 시 세션 아이디 새로 생성, 세션 내용도 변경
                ;

        // 세션 정책
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // 스프링 시큐리티가 필요 시 세션 생성(기본값)
//                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)       // 스프링 시큐리티가 항상 세션 생성
//                .sessionCreationPolicy(SessionCreationPolicy.NEVER)        // 스프링 시큐리티가 생성하진 않지만 이미 존재하면 사용
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)    // 스프링 시큐리티가 생성하지도 않고 존재해도 사용X
                ;

    }
}
