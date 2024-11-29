package com.nhnacademy.minidooray3team.dto;

import com.nhnacademy.minidooray3team.domain.Status;
import lombok.Data;

@Data
public class AccountModifyDto {
    private String username;
    private Status status;
}
