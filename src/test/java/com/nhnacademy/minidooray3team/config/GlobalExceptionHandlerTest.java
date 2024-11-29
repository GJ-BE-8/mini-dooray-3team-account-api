package com.nhnacademy.minidooray3team;

import com.nhnacademy.minidooray3team.config.GlobalExceptionHandler;
import com.nhnacademy.minidooray3team.domain.Account;
import com.nhnacademy.minidooray3team.domain.Role;
import com.nhnacademy.minidooray3team.domain.Status;
import com.nhnacademy.minidooray3team.exception.AccountAlreadyExistsException;
import com.nhnacademy.minidooray3team.exception.AccountNotFoundException;
import com.nhnacademy.minidooray3team.service.AccountService;
import com.nhnacademy.minidooray3team.dto.AccountRegisterDto;
import com.nhnacademy.minidooray3team.repository.AccountRepository;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(globalExceptionHandler).build();
    }

    @Test
    void testHandleAccountAlreadyExistsException() throws Exception {
        String email = "user@example.com";

        Account account = new Account("user","user@example.com","123456", Status.ACTIVE, Role.MEMBER, LocalDateTime.now());
        // Mock accountRepository to simulate existing emai
        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));

        mockMvc.perform(post("/accounts")  // Ensure this matches your controller's mapping
                        .contentType("application/json")
                        .content("{\"email\":\"" + email + "\", \"username\":\"username\", \"password\":\"password\"}"))
                .andExpect(status().isConflict())  // 409 Conflict status
                .andExpect(content().string("이미 존재하는 이메일입니다."));
    }

    @Test
    void testHandleAccountNotFoundException() throws Exception {
        // 테스트할 메서드 설정
        when(accountRepository.findByAccountId(anyLong())).thenThrow(new AccountNotFoundException("존재하지 않는 계정입니다."));

        mockMvc.perform(get("/accounts/1"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 코드
                .andExpect(content().string("존재하지 않는 계정입니다.")); // 예외 메시지 확인
    }

    @Test
    void testHandleMethodArgumentNotValidException() throws Exception {
        // 예시: DTO 유효성 검사 오류 발생
        mockMvc.perform(post("/accounts/register")
                        .contentType("application/json")
                        .content("{\"email\":\"\", \"username\":\"\", \"password\":\"\"}"))
                .andExpect(status().isBadRequest()) // HTTP 400 상태 코드
                .andExpect(jsonPath("$.email").value("이메일은 필수입니다.")) // 유효성 검사 실패 필드
                .andExpect(jsonPath("$.username").value("이름은 필수입니다."))
                .andExpect(jsonPath("$.password").value("비밀번호는 필수입니다."));
    }
}
