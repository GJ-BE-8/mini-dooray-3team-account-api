package com.nhnacademy.minidooray3team.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountRegistrationRequest {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Size(min = 3, max = 20, message = "아이디는 3자 이상 20자 이하로 입력하세요.")
    private String username;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String password;
}

