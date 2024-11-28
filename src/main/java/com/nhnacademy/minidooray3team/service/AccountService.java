package com.nhnacademy.minidooray3team.service;


import com.nhnacademy.minidooray3team.domain.Account;
import com.nhnacademy.minidooray3team.domain.Status;
import com.nhnacademy.minidooray3team.dto.AccountModifyDto;
import com.nhnacademy.minidooray3team.dto.AccountRegisterDto;
import com.nhnacademy.minidooray3team.exception.AccountAlreadyExistsException;
import com.nhnacademy.minidooray3team.exception.AccountNotFoundException;
import com.nhnacademy.minidooray3team.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


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

        // 이름 수정
        if (accountModifyDto.getUsername() != null && !accountModifyDto.getUsername().isEmpty()) {
            account.setUsername(accountModifyDto.getUsername());
        }

        // 비밀번호 수정
        if (accountModifyDto.getPassword() != null && !accountModifyDto.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(accountModifyDto.getPassword());
            account.setPassword(hashedPassword);
        }

        // 상태 수정
        if (accountModifyDto.getStatus() != null) {
            account.setStatus(accountModifyDto.getStatus());
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
}
