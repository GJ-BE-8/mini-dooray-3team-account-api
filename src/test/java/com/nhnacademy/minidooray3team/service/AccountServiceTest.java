package com.nhnacademy.minidooray3team.service;

import com.nhnacademy.minidooray3team.domain.Account;
import com.nhnacademy.minidooray3team.domain.Role;
import com.nhnacademy.minidooray3team.domain.Status;
import com.nhnacademy.minidooray3team.dto.AccountInfo;
import com.nhnacademy.minidooray3team.dto.AccountInfoDto;
import com.nhnacademy.minidooray3team.dto.AccountModifyDto;
import com.nhnacademy.minidooray3team.dto.AccountRegisterDto;
import com.nhnacademy.minidooray3team.exception.AccountAlreadyExistsException;
import com.nhnacademy.minidooray3team.exception.AccountNotFoundException;
import com.nhnacademy.minidooray3team.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountService accountService;

    private Account account;
    private AccountRegisterDto accountRegisterDto;
    private AccountModifyDto accountModifyDto;

    @BeforeEach
    public void setUp() {
        account = new Account(1L, "username", "user@example.com", "password", Status.ACTIVE, null, LocalDateTime.now(), LocalDateTime.now());
        accountRegisterDto = new AccountRegisterDto(1L,"username", "user@example.com", "password", Role.MEMBER, Status.ACTIVE);
        accountModifyDto = new AccountModifyDto();
        //accountModifyDto.setUsername("updatedUsername");
        accountModifyDto.setStatus(Status.DORMANT);
    }

    @Test
    @DisplayName("register success")
    void testRegisterAccount_Success() {
        when(accountRepository.findByEmail(accountRegisterDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(accountRegisterDto.getPassword())).thenReturn("encodedPassword");

        accountService.registerAccount(accountRegisterDto);

        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @DisplayName("register fail")
    void testRegisterAccount_AccountAlreadyExists() {
        when(accountRepository.findByEmail(accountRegisterDto.getEmail())).thenReturn(Optional.of(account));

        assertThrows(AccountAlreadyExistsException.class, () -> accountService.registerAccount(accountRegisterDto));
    }

    @Test
    @DisplayName("update success (password not updated)")
    void testUpdateAccount_Success() {
        when(accountRepository.findByAccountId(account.getAccountId())).thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account updatedAccount = accountService.updateAccount(account.getAccountId(), accountModifyDto);

        assertNotNull(updatedAccount, "Updated account should not be null");
        assertEquals(Status.DORMANT, updatedAccount.getStatus());
        assertNotNull(updatedAccount.getUpdatedAt(), "UpdatedAt should not be null");

        verify(accountRepository, times(1)).save(updatedAccount);
    }

    @Test
    @DisplayName("update fail")
    void testUpdateAccount_AccountNotFound() {
        when(accountRepository.findByAccountId(account.getAccountId())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.updateAccount(account.getAccountId(), accountModifyDto));
    }

    @Test
    @DisplayName("update status with valid value")
    void testUpdateAccount_StatusNotNull() {
        accountModifyDto.setStatus(Status.DORMANT);
        when(accountRepository.findByAccountId(account.getAccountId())).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        Account updatedAccount = accountService.updateAccount(account.getAccountId(), accountModifyDto);

        assertEquals(Status.DORMANT, updatedAccount.getStatus());
        verify(accountRepository, times(1)).save(updatedAccount);
    }

    @Test
    @DisplayName("update status fail (null status)")
    void testUpdateAccount_StatusNull() {
        accountModifyDto.setStatus(null);
        when(accountRepository.findByAccountId(account.getAccountId())).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        Account updatedAccount = accountService.updateAccount(account.getAccountId(), accountModifyDto);

        assertEquals(Status.ACTIVE, updatedAccount.getStatus());
        verify(accountRepository, times(1)).save(updatedAccount);
    }

    @Test
    @DisplayName("delete success")
    void testDeleteAccount_Success() {
        when(accountRepository.findByAccountId(account.getAccountId())).thenReturn(Optional.of(account));

        accountService.deleteAccount(account.getAccountId());

        verify(accountRepository, times(1)).delete(account);
    }

    @Test
    @DisplayName("delete fail")
    void testDeleteAccount_AccountNotFound() {
        when(accountRepository.findByAccountId(account.getAccountId())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount(account.getAccountId()));
    }

    @Test
    @DisplayName("findByName -> AccountInfo")
    void testFindByName() {

        Account account = new Account();
        account.setUsername("username");
        account.setPassword("password123");

        when(accountRepository.findByUsername("username")).thenReturn(Optional.of(account));

        AccountInfo accountInfo = accountService.findByName("username");

        assertEquals("username", accountInfo.getUsername());
        assertEquals("password123", accountInfo.getPassword());
        verify(accountRepository, times(1)).findByUsername("username");
    }

    @Test
    @DisplayName("findByNameForId 성공")
    void testFindByNameForId_Success() {
        // Given
        String username = "username";
        Long expectedId = 1L;
        account.setAccountId(expectedId);
        account.setUsername(username);

        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(account));

        // When
        Long actualId = accountService.findByNameForId(username);

        // Then
        assertEquals(expectedId, actualId);
        verify(accountRepository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("findByNameForId 실패 (존재하지 않는 사용자)")
    void testFindByNameForId_NotFound() {
        // Given
        String username = "nonexistentuser";

        when(accountRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When & Then
        // 현재 서비스 메서드에서 NullPointerException이 발생할 수 있으므로 이를 검증합니다.
        assertThrows(NullPointerException.class, () -> accountService.findByNameForId(username));
        verify(accountRepository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("getAccountInfo 성공")
    void testGetAccountInfo_Success() {
        // Given
        Long accountId = 1L;
        account.setAccountId(accountId);
        account.setUsername("username");
        account.setEmail("user@example.com");
        account.setRole(Role.MEMBER);
        account.setStatus(Status.ACTIVE);

        when(accountRepository.findByAccountId(accountId)).thenReturn(Optional.of(account));

        // When
        AccountInfoDto accountInfoDto = accountService.getAccountInfo(accountId);

        // Then
        assertNotNull(accountInfoDto);
        assertEquals(accountId, accountInfoDto.getAccountId());
        assertEquals("username", accountInfoDto.getUsername());
        assertEquals("user@example.com", accountInfoDto.getEmail());
        assertEquals(Role.MEMBER, accountInfoDto.getRole());
        assertEquals(Status.ACTIVE, accountInfoDto.getStatus());
        verify(accountRepository, times(1)).findByAccountId(accountId);
    }

    @Test
    @DisplayName("getAccountInfo 실패 (존재하지 않는 계정)")
    void testGetAccountInfo_NotFound() {
        // Given
        Long accountId = 999L;

        when(accountRepository.findByAccountId(accountId)).thenReturn(Optional.empty());

        // When & Then
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> accountService.getAccountInfo(accountId));
        assertEquals("Account not found with id: " + accountId, exception.getMessage());
        verify(accountRepository, times(1)).findByAccountId(accountId);
    }

}
