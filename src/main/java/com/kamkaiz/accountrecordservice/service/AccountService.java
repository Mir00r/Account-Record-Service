package com.kamkaiz.accountrecordservice.service;

import com.kamkaiz.accountrecordservice.dto.AccountDTO;
import com.kamkaiz.accountrecordservice.dto.AccountUpdateDTO;
import com.kamkaiz.accountrecordservice.mapper.AccountMapper;
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
    private final AccountMapper accountMapper;

    /**
     * Get all accounts with pagination.
     *
     * @param pageable Pagination information
     * @return Page of AccountDTO objects
     */
    @Transactional(readOnly = true)
    public Page<AccountDTO> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable)
                .map(accountMapper::toDTO);
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
        return accountMapper.toDTO(account);
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
                .map(accountMapper::toDTO);
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
                .map(accountMapper::toDTO);
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
                .map(accountMapper::toDTO);
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
        
        return accountMapper.toDTO(updatedAccount);
    }
}
