package com.nhnacademy.minidooray3team.dto;

import lombok.Data;

import java.time.LocalDateTime;
import com.nhnacademy.minidooray3team.domain.Status;
import com.nhnacademy.minidooray3team.domain.Role;


@Data
public class AccountDto {
    private String username;
    private String email;
    private String password;
    private Status status; // status ENUM('ACTIVE', 'DORMANT', 'DELETED'),
    private Role role; // ROLE_ADMIN, ROLE_MEMBER
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

