package com.study.springsecurity.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/greeting")
    public String greeting() {
        return "hello";
    }
}
