package com.nhnacademy.minidooray3team.controller;

import com.nhnacademy.minidooray3team.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FindIdController {
    private final AccountService accountService;

    public FindIdController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/getId/{name}")
    public Long getId(@PathVariable String name) {
        return accountService.findByNameForId(name);
    }
}
