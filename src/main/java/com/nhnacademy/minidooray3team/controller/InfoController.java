package com.nhnacademy.minidooray3team.controller;

import com.nhnacademy.minidooray3team.dto.AccountInfoDto;
import com.nhnacademy.minidooray3team.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    private final AccountService accountService;

    public InfoController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/accounts/info/{accountId}")
    public ResponseEntity<AccountInfoDto> showAccountInfo(@PathVariable Long accountId){
        return ResponseEntity.ok(accountService.getAccountInfo(accountId));
    }
}
