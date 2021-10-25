package com.study.springsecurity.web.teacher;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;

@Component
public class TeacherManager implements AuthenticationProvider, InitializingBean {

    // DB 연동
    private final HashMap<String, Teacher> teacherDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        TeacherAuthenticationToken token = (TeacherAuthenticationToken) authentication;

        // 토큰의 정보가 DB에 있으면 StudentAuthenticationToken 발급
       if (teacherDB.containsKey(token.getCredentials())) {
            Teacher teacher = teacherDB.get(token.getCredentials());
            return TeacherAuthenticationToken.builder()
                    .principal(teacher)
                    .details(teacher.getUsername())
                    .authenticated(true)
                    .build();
        }
        return null; // 처리할 수 없는 인증은 null 을 리턴
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == TeacherAuthenticationToken.class; // 로그인 시 처리할 토큰 타입 설정
    }

    // 객체 생성시 수행 메소드 ( 초기 데이터 설정 )
    @Override
    public void afterPropertiesSet() throws Exception {
        Teacher teacher = new Teacher("choi", "최선생", Collections.singleton(new SimpleGrantedAuthority("ROLE_TEACHER")));

        teacherDB.put(teacher.getId(), teacher);
    }
}
