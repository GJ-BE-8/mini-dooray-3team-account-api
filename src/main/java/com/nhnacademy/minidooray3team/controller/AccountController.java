package com.nhnacademy.minidooray3team.controller;

import com.nhnacademy.minidooray3team.domain.Account;
import com.nhnacademy.minidooray3team.dto.AccountModifyDto;
import com.nhnacademy.minidooray3team.dto.AccountRegisterDto;
import com.nhnacademy.minidooray3team.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }
    @PostMapping
    public ResponseEntity<String> register(@RequestBody @Valid AccountRegisterDto accountDto) {
        accountService.registerAccount(accountDto);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @PutMapping(value = "/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> updateAccount(@PathVariable Long accountId, @RequestBody  @Valid AccountModifyDto accountModifyDto) {
        Account updatedAccount = accountService.updateAccount(accountId, accountModifyDto);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok("Account deleted successfully.");
    }
}


