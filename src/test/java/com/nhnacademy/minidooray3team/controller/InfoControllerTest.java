package com.nhnacademy.minidooray3team.controller;

import com.nhnacademy.minidooray3team.config.GlobalExceptionHandler;
import com.nhnacademy.minidooray3team.domain.Role;
import com.nhnacademy.minidooray3team.domain.Status;
import com.nhnacademy.minidooray3team.dto.AccountInfoDto;
import com.nhnacademy.minidooray3team.exception.AccountNotFoundException;
import com.nhnacademy.minidooray3team.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class InfoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private InfoController infoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(infoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testShowAccountInfo_Success() throws Exception {
        // Test data
        Long accountId = 1L;
        AccountInfoDto accountInfoDto = new AccountInfoDto(
                accountId,
                "username",
                "email@example.com",
                Role.MEMBER,
                Status.ACTIVE
        );

        // Mock service behavior
        when(accountService.getAccountInfo(accountId)).thenReturn(accountInfoDto);

        // Perform GET request and verify results
        mockMvc.perform(get("/accounts/info/{accountId}", accountId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value(accountId))
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.email").value("email@example.com"))
                .andExpect(jsonPath("$.role").value("MEMBER"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void testShowAccountInfo_NotFound() throws Exception {
        Long accountId = 999L; // Non-existent ID

        // Mock service to throw exception
        when(accountService.getAccountInfo(accountId))
                .thenThrow(new AccountNotFoundException("Account not found with id: " + accountId));

        // Perform GET request and verify results
        mockMvc.perform(get("/accounts/info/{accountId}", accountId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Account not found with id: " + accountId));
    }
}
