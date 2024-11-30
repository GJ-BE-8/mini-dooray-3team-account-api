package com.nhnacademy.minidooray3team.dto;

import com.nhnacademy.minidooray3team.domain.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountModifyDto {
//    @NotBlank(message = "이름은 필수 입력 값입니다.")
//    private String username;
    private Status status;
}
