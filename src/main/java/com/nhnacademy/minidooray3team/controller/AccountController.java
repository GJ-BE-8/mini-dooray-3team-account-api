package com.nhnacademy.minidooray3team.controller;

import com.nhnacademy.minidooray3team.dto.AccountDto;
import com.nhnacademy.minidooray3team.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // 회원가입 페이지
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid AccountDto accountDto) {
        accountService.registerUser(accountDto);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
}


