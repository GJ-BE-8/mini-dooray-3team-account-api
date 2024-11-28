package com.nhnacademy.minidooray3team.service;


import com.nhnacademy.minidooray3team.domain.Account;
import com.nhnacademy.minidooray3team.domain.Role;
import com.nhnacademy.minidooray3team.domain.Status;
import com.nhnacademy.minidooray3team.dto.AccountRegistrationRequest;
import com.nhnacademy.minidooray3team.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(AccountRegistrationRequest request) {
        // 사용자 중복 체크
        accountRepository.findByUsername(request.getUsername())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
                });

        accountRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
                });

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 사용자 저장
        Account account = new Account(
                null,
                request.getUsername(),
                request.getEmail(),
                encodedPassword,
                Status.ACTIVE,
                Role.MEMBER,
                LocalDateTime.now(),
                null
        );
        accountRepository.save(account);
    }
}
