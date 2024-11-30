package com.nhnacademy.minidooray3team.service;

import com.nhnacademy.minidooray3team.domain.Account;
import com.nhnacademy.minidooray3team.domain.Role;
import com.nhnacademy.minidooray3team.domain.Status;
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
        assertEquals("updatedUsername", updatedAccount.getUsername());
        assertEquals(account.getPassword(), updatedAccount.getPassword());
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

//    @ParameterizedTest
//    @CsvSource({
//            "'newUsername', 'newUsername'",  // valid username
//            "'', 'username'"                // empty username
//    })
//    @DisplayName("update username based on input value")
//    void testUpdateAccount_Username(String inputUsername, String expectedUsername) {
//        accountModifyDto.setUsername(inputUsername);
//        when(accountRepository.findByAccountId(account.getAccountId())).thenReturn(Optional.of(account));
//        when(accountRepository.save(account)).thenReturn(account);
//
//        Account updatedAccount = accountService.updateAccount(account.getAccountId(), accountModifyDto);
//
//        assertEquals(expectedUsername, updatedAccount.getUsername());
//        verify(accountRepository, times(1)).save(updatedAccount);
//    }


//    @Test
//    @DisplayName("update username fail (empty null)")
//    void testUpdateAccount_UsernameNull() {
//        accountModifyDto.setUsername(null);
//        when(accountRepository.findByAccountId(account.getAccountId())).thenReturn(Optional.of(account));
//        when(accountRepository.save(account)).thenReturn(account);
//
//        Account updatedAccount = accountService.updateAccount(account.getAccountId(), accountModifyDto);
//
//        assertEquals("username", updatedAccount.getUsername());
//        verify(accountRepository, times(1)).save(updatedAccount);
//    }
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
    @DisplayName("findbyUserName -> AccountInfo")
    void testfindUserName() {
        when(accountRepository.findByEmail(account.getUsername())).thenReturn(Optional.of(account));
        accountService.findByName("username");
        assertEquals("username", account.getUsername());
        verify(accountRepository, times(1)).findByEmail(account.getUsername());
    }
}
//account = new Account(1L, "username", "user@example.com", "password", Status.ACTIVE, null, LocalDateTime.now(), LocalDateTime.now());