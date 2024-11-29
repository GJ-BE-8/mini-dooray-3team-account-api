package com.nhnacademy.minidooray3team.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.minidooray3team.domain.Role;
import com.nhnacademy.minidooray3team.domain.Status;
import com.nhnacademy.minidooray3team.dto.AccountModifyDto;
import com.nhnacademy.minidooray3team.dto.AccountRegisterDto;
import com.nhnacademy.minidooray3team.service.AccountService;
import com.nhnacademy.minidooray3team.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("register success")
    void testRegisterAccount_Success() throws Exception {
        AccountRegisterDto accountRegisterDto = new AccountRegisterDto();
        accountRegisterDto.setUsername("John Doe");
        accountRegisterDto.setEmail("john.doe@example.com");
        accountRegisterDto.setPassword("password123");
        accountRegisterDto.setRole(Role.MEMBER);
        accountRegisterDto.setStatus(Status.ACTIVE);

        doNothing().when(accountService).registerAccount(any(AccountRegisterDto.class));

        mockMvc.perform(post("/accounts")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRegisterDto))
                        .accept("application/json")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("회원가입이 완료되었습니다."));
    }

    @Test
    @DisplayName("update success")
    void testUpdateAccount_Success() throws Exception {
        AccountModifyDto accountModifyDto = new AccountModifyDto();
        accountModifyDto.setUsername("Updated Name");
        accountModifyDto.setStatus(Status.ACTIVE);

        // 수정된 Account
        Account updatedAccount = new Account(1L, "Updated Name", "john.doe@example.com", "newpassword123", Status.ACTIVE, Role.MEMBER, LocalDateTime.now(), LocalDateTime.now());

        when(accountService.updateAccount(1L, accountModifyDto)).thenReturn(updatedAccount);
        mockMvc.perform(put("/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountModifyDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("Updated Name"))
                .andExpect(jsonPath("$.status").value("active"))
                .andExpect(jsonPath("$.password").value("newpassword123"));
    }

    @Test
    @DisplayName("delete success")
    void testDeleteAccount_Success() throws Exception {
        doNothing().when(accountService).deleteAccount(1L);

        mockMvc.perform(delete("/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account deleted successfully."));
    }
}