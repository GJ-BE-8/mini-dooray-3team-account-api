package com.nhnacademy.minidooray3team.service;


import com.nhnacademy.minidooray3team.domain.Account;
import com.nhnacademy.minidooray3team.domain.Role;
import com.nhnacademy.minidooray3team.domain.Status;
import com.nhnacademy.minidooray3team.dto.AccountDto;
import com.nhnacademy.minidooray3team.exception.AccountAlreadyExistsException;
import com.nhnacademy.minidooray3team.dto.AccountRegistrationRequest;
import com.nhnacademy.minidooray3team.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;



@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;


    public void registerUser(AccountDto accountDto) {
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
}
