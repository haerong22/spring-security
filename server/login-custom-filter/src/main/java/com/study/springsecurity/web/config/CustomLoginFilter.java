package com.study.springsecurity.web.config;

import com.study.springsecurity.web.student.StudentAuthenticationToken;
import com.study.springsecurity.web.teacher.TeacherAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    public CustomLoginFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        username = (username != null) ? username : "";
        username = username.trim();
        String password = obtainPassword(request);
        password = (password != null) ? password : "";
        String type = request.getParameter("type");

        // type 에 따라서 다른 토큰 발급
        if (type == null || !type.equals("teacher")) {
            // student
            StudentAuthenticationToken token = StudentAuthenticationToken.builder()
                    .credentials(username).build();
            return this.getAuthenticationManager().authenticate(token);
        } else {
            // teacher
            TeacherAuthenticationToken token = TeacherAuthenticationToken.builder()
                    .credentials(username).build();

            return this.getAuthenticationManager().authenticate(token);
        }
    }
}
