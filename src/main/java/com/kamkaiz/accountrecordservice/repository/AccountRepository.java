package com.kamkaiz.accountrecordservice.repository;

import com.kamkaiz.accountrecordservice.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Optional<Account> findByAccountNumber(String accountNumber);
    
    Page<Account> findByCustomerId(String customerId, Pageable pageable);
    
    Page<Account> findByAccountNumberIn(List<String> accountNumbers, Pageable pageable);
    
    Page<Account> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);
    
    @Lock(LockModeType.OPTIMISTIC)
    Optional<Account> findWithLockingByAccountNumber(String accountNumber);
}