package com.nhnacademy.minidooray3team.controller;

import com.nhnacademy.minidooray3team.dto.AccountRegistrationRequest;
import com.nhnacademy.minidooray3team.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // 로그인 페이지
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // 회원가입 페이지
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid AccountRegistrationRequest request) {
        accountService.registerUser(request);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
}


