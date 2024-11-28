package com.nhnacademy.minidooray3team.dto;

import com.nhnacademy.minidooray3team.domain.Status;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountModifyDto {
    private String username;
    private Status status;

    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String password;
}
