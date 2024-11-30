package com.nhnacademy.minidooray3team.service;


import com.nhnacademy.minidooray3team.domain.Account;
import com.nhnacademy.minidooray3team.domain.Status;
import com.nhnacademy.minidooray3team.dto.AccountInfo;
import com.nhnacademy.minidooray3team.dto.AccountModifyDto;
import com.nhnacademy.minidooray3team.dto.AccountRegisterDto;
import com.nhnacademy.minidooray3team.exception.AccountAlreadyExistsException;
import com.nhnacademy.minidooray3team.exception.AccountNotFoundException;
import com.nhnacademy.minidooray3team.repository.AccountRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerAccount(AccountRegisterDto accountDto) {
        //이메일 중복 체크
        accountRepository.findByEmail(accountDto.getEmail())
                .ifPresent(user -> {
                    throw new AccountAlreadyExistsException("이미 존재하는 이메일입니다.");
                });

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(accountDto.getPassword());

        // 사용자 저장
        Account account = new Account(
                accountDto.getUsername(),
                accountDto.getEmail(),
                encodedPassword,
                Status.ACTIVE,
                accountDto.getRole(),
                LocalDateTime.now());
        accountRepository.save(account);
    }


    @Transactional
    public Account updateAccount(Long accountId, AccountModifyDto accountModifyDto) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AccountNotFoundException("존재하지 않는 계정입니다."));
        if (Objects.nonNull(accountModifyDto.getUsername()) && !accountModifyDto.getUsername().isEmpty()) {
            account.setUsername(accountModifyDto.getUsername());
        } else {
            account.setUsername(account.getUsername());
        }

        if (Objects.nonNull(accountModifyDto.getStatus())) {
            account.setStatus(accountModifyDto.getStatus());
        } else {
            account.setStatus(account.getStatus());
        }

        account.setUpdatedAt(LocalDateTime.now());

        return accountRepository.save(account);
    }


    @Transactional
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AccountNotFoundException("존재하지 않는 계정입니다."));

        accountRepository.delete(account);
    }

    @Transactional
    public AccountInfo findByName(String name) {
        Optional<Account> byUsername = accountRepository.findByUsername(name);
        Account account = byUsername.orElse(null);
        return new AccountInfo(account.getUsername(), account.getPassword());
    }
}
