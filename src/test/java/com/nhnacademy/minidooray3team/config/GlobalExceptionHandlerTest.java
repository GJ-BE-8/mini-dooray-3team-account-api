package com.nhnacademy.minidooray3team.config;

import com.nhnacademy.minidooray3team.domain.Account;
import com.nhnacademy.minidooray3team.domain.Role;
import com.nhnacademy.minidooray3team.domain.Status;
import com.nhnacademy.minidooray3team.dto.AccountModifyDto;
import com.nhnacademy.minidooray3team.exception.AccountNotFoundException;
import com.nhnacademy.minidooray3team.service.AccountService;
import com.nhnacademy.minidooray3team.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountRepository accountRepository;

    @Mock
    private AccountService accountService;

    @Test
    void testHandleAccountAlreadyExistsException() throws Exception {
        String email = "user@example.com";

        Account account = new Account("user", "user@example.com", "123456", Status.ACTIVE, Role.MEMBER, LocalDateTime.now());
        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));

        mockMvc.perform(post("/accounts")
                        .contentType("application/json")
                        .content("{\"email\":\"" + email + "\", \"username\":\"username\", \"password\":\"password\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("이미 존재하는 이메일입니다."));
    }


    @Test
    void testHandleAccountNotFoundException() throws Exception {
        Long accountId = 1L;

        when(accountService.updateAccount(eq(accountId), any(AccountModifyDto.class)))
                .thenThrow(new AccountNotFoundException("존재하지 않는 계정입니다."));

        mockMvc.perform(post("/accounts/{accountId}", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"ACTIVE\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("존재하지 않는 계정입니다."));
    }

    @Test
    void testHandleMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(post("/accounts")
                        .contentType("application/json")
                        .content("{\"email\":\"\", \"username\":\"\", \"password\":\"pass\"}"))
                .andExpect(status().isBadRequest()) // HTTP 400 상태 코드
                .andExpect(jsonPath("$.email").value("이메일은 필수 입력 값입니다.")) // 유효성 검사 실패 필드
                .andExpect(jsonPath("$.username").value("이름은 필수 입력 값입니다."))
                .andExpect(jsonPath("$.password").value("비밀번호는 최소 6자 이상이어야 합니다."));
    }

    @Test
    void testHandleInvalidEmailFormat() throws Exception {
        // 잘못된 이메일 형식 예시 (예: "userexample.com"은 형식이 잘못됨)
        String invalidEmailDto = "{\"email\":\"userexample.com\", \"username\":\"username\", \"password\":\"password123\"}";

        mockMvc.perform(post("/accounts")
                        .contentType("application/json")
                        .content(invalidEmailDto))
                .andExpect(status().isBadRequest()) // HTTP 400 상태 코드
                .andExpect(jsonPath("$.email").value("올바른 이메일 형식이어야 합니다.")); // 이메일 형식 검증 실패 메시지
    }
}
