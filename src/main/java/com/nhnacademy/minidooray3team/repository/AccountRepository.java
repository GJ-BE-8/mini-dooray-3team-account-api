package com.nhnacademy.minidooray3team.repository;



import com.nhnacademy.minidooray3team.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountId(Long accountId);
    Optional<Account> findByEmail(String email);

    Optional<Account> findByUsername(String username);
}
