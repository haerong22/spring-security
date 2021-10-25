package com.study.springsecurity.web.student;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentManager implements AuthenticationProvider, InitializingBean {

    // DB 연동
    private final HashMap<String, Student> studentDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        StudentAuthenticationToken token = (StudentAuthenticationToken) authentication;

        // 토큰의 정보가 DB에 있으면 StudentAuthenticationToken 발급
       if (studentDB.containsKey(token.getCredentials())) {
            Student student = studentDB.get(token.getCredentials());
            return StudentAuthenticationToken.builder()
                    .principal(student)
                    .details(student.getUsername())
                    .authenticated(true)
                    .build();
        }
        return null; // 처리할 수 없는 인증은 null 을 리턴
    }

    public List<Student> myStudentList(String teacherId) {
        return studentDB.values().stream().filter(s -> s.getTeacherId().equals(teacherId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == StudentAuthenticationToken.class; // 로그인 시 처리할 토큰 타입 설정
    }

    // 객체 생성시 수행 메소드 ( 초기 데이터 설정 )
    @Override
    public void afterPropertiesSet() throws Exception {
        Student student = new Student("hong", "홍길동", Collections.singleton(new SimpleGrantedAuthority("ROLE_STUDENT")), "choi");
        Student student1 = new Student("kang", "강아지", Collections.singleton(new SimpleGrantedAuthority("ROLE_STUDENT")), "choi");
        Student student2 = new Student("rang", "호랑이", Collections.singleton(new SimpleGrantedAuthority("ROLE_STUDENT")), "choi");

        studentDB.put(student.getId(), student);
        studentDB.put(student1.getId(), student1);
        studentDB.put(student2.getId(), student2);
    }
}
