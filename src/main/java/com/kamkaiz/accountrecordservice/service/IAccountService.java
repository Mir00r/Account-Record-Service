package com.kamkaiz.accountrecordservice.service;

import com.kamkaiz.accountrecordservice.dto.AccountDTO;
import com.kamkaiz.accountrecordservice.dto.AccountUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for account operations.
 * Follows Interface Segregation Principle by providing focused account management operations.
 */
public interface IAccountService {
    
    /**
     * Get all accounts with pagination.
     *
     * @param pageable Pagination information
     * @return Page of AccountDTO objects
     */
    Page<AccountDTO> getAllAccounts(Pageable pageable);

    /**
     * Get account by account number.
     *
     * @param accountNumber Account number to search for
     * @return AccountDTO object
     */
    AccountDTO getAccountByAccountNumber(String accountNumber);

    /**
     * Get accounts by customer ID with pagination.
     *
     * @param customerId Customer ID to search for
     * @param pageable   Pagination information
     * @return Page of AccountDTO objects
     */
    Page<AccountDTO> getAccountsByCustomerId(String customerId, Pageable pageable);

    /**
     * Get accounts by a list of account numbers with pagination.
     *
     * @param accountNumbers List of account numbers to search for
     * @param pageable       Pagination information
     * @return Page of AccountDTO objects
     */
    Page<AccountDTO> getAccountsByAccountNumbers(List<String> accountNumbers, Pageable pageable);

    /**
     * Get accounts by description (partial match) with pagination.
     *
     * @param description Description to search for
     * @param pageable    Pagination information
     * @return Page of AccountDTO objects
     */
    Page<AccountDTO> getAccountsByDescription(String description, Pageable pageable);

    /**
     * Update account description.
     *
     * @param accountNumber    Account number to update
     * @param accountUpdateDTO DTO containing the new description
     * @return Updated AccountDTO
     */
    AccountDTO updateAccountDescription(String accountNumber, AccountUpdateDTO accountUpdateDTO);
}