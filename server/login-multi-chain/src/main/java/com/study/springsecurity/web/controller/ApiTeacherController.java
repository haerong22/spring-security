package com.study.springsecurity.web.controller;

import com.study.springsecurity.web.student.Student;
import com.study.springsecurity.web.student.StudentManager;
import com.study.springsecurity.web.teacher.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/teacher")
public class ApiTeacherController {

    @Autowired
    StudentManager studentManager;

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")
    @GetMapping("/students")
    public List<Student> main(@AuthenticationPrincipal Teacher teacher){
        return studentManager.myStudentList(teacher.getId());
    }


}
