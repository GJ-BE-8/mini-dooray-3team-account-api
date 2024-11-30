package com.nhnacademy.minidooray3team.controller;

import com.nhnacademy.minidooray3team.dto.AccountInfo;
import com.nhnacademy.minidooray3team.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final AccountService accountService;

    public LoginController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping("/accounts/{email}")
    public AccountInfo getUserCredentials(@PathVariable String email) {
        return accountService.findByEmail(email);
    }
}

