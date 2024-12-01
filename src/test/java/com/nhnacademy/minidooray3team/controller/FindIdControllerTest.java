package com.nhnacademy.minidooray3team.controller;

import com.nhnacademy.minidooray3team.config.GlobalExceptionHandler;
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


class FindIdControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private FindIdController findIdController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(findIdController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testGetId_Success() throws Exception {
        // 테스트 데이터 준비
        String username = "testuser";
        Long expectedId = 1L;

        // 서비스 동작 모킹
        when(accountService.findByNameForId(username)).thenReturn(expectedId);

        // GET 요청 및 결과 검증
        mockMvc.perform(get("/getId/{name}", username)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedId.toString()));
    }

    @Test
    void testGetId_NotFound() throws Exception {
        // 테스트 데이터 준비
        String username = "nonexistentuser";

        // 서비스에서 예외를 발생시키도록 모킹
        when(accountService.findByNameForId(username))
                .thenThrow(new AccountNotFoundException("Account not found with name: " + username));

        // GET 요청 및 결과 검증
        mockMvc.perform(get("/getId/{name}", username)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Account not found with name: " + username));
    }
}
