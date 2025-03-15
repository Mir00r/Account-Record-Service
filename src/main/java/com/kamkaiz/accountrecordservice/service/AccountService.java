package com.kamkaiz.accountrecordservice.service;

import com.kamkaiz.accountrecordservice.dto.AccountDTO;
import com.kamkaiz.accountrecordservice.dto.AccountUpdateDTO;
import com.kamkaiz.accountrecordservice.model.Account;
import com.kamkaiz.accountrecordservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * Service for handling account-related operations.
 * This service provides methods for retrieving and updating account records.
 */
@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;

    /**
     * Get all accounts with pagination.
     *
     * @param pageable Pagination information
     * @return Page of AccountDTO objects
     */
    @Transactional(readOnly = true)
    public Page<AccountDTO> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    /**
     * Get account by account number.
     *
     * @param accountNumber Account number to search for
     * @return AccountDTO object
     * @throws EntityNotFoundException if account not found
     */
    @Transactional(readOnly = true)
    public AccountDTO getAccountByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with account number: " + accountNumber));
        return mapToDTO(account);
    }

    /**
     * Get accounts by customer ID with pagination.
     *
     * @param customerId Customer ID to search for
     * @param pageable   Pagination information
     * @return Page of AccountDTO objects
     */
    @Transactional(readOnly = true)
    public Page<AccountDTO> getAccountsByCustomerId(String customerId, Pageable pageable) {
        return accountRepository.findByCustomerId(customerId, pageable)
                .map(this::mapToDTO);
    }

    /**
     * Get accounts by a list of account numbers with pagination.
     *
     * @param accountNumbers List of account numbers to search for
     * @param pageable       Pagination information
     * @return Page of AccountDTO objects
     */
    @Transactional(readOnly = true)
    public Page<AccountDTO> getAccountsByAccountNumbers(List<String> accountNumbers, Pageable pageable) {
        return accountRepository.findByAccountNumberIn(accountNumbers, pageable)
                .map(this::mapToDTO);
    }

    /**
     * Get accounts by description (partial match) with pagination.
     *
     * @param description Description to search for
     * @param pageable    Pagination information
     * @return Page of AccountDTO objects
     */
    @Transactional(readOnly = true)
    public Page<AccountDTO> getAccountsByDescription(String description, Pageable pageable) {
        return accountRepository.findByDescriptionContainingIgnoreCase(description, pageable)
                .map(this::mapToDTO);
    }

    /**
     * Update account description.
     * Uses optimistic locking to handle concurrent updates.
     *
     * @param accountNumber   Account number to update
     * @param accountUpdateDTO DTO containing the new description
     * @return Updated AccountDTO
     * @throws EntityNotFoundException if account not found
     */
    @Transactional
    public AccountDTO updateAccountDescription(String accountNumber, AccountUpdateDTO accountUpdateDTO) {
        Account account = accountRepository.findWithLockingByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with account number: " + accountNumber));
        
        account.setDescription(accountUpdateDTO.getDescription());
        Account updatedAccount = accountRepository.save(account);
        
        return mapToDTO(updatedAccount);
    }

    /**
     * Map Account entity to AccountDTO.
     *
     * @param account Account entity
     * @return AccountDTO
     */
    private AccountDTO mapToDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .customerId(account.getCustomerId())
                .balance(account.getBalance())
                .description(account.getDescription())
                .build();
    }
}