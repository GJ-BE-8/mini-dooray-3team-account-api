package com.nhnacademy.minidooray3team.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // 로그인 페이지
    }
}
