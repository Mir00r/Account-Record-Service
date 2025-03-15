package com.kamkaiz.accountrecordservice.controller;

import com.kamkaiz.accountrecordservice.dto.AccountDTO;
import com.kamkaiz.accountrecordservice.dto.AccountUpdateDTO;
import com.kamkaiz.accountrecordservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller for handling account-related requests.
 * This controller provides endpoints for retrieving and updating account records.
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * Get all accounts with pagination.
     *
     * @param pageable Pagination information
     * @return Page of AccountDTO objects
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<AccountDTO>> getAllAccounts(Pageable pageable) {
        return ResponseEntity.ok(accountService.getAllAccounts(pageable));
    }

    /**
     * Get account by account number.
     *
     * @param accountNumber Account number to search for
     * @return AccountDTO object
     */
    @GetMapping("/{accountNumber}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AccountDTO> getAccountByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccountByAccountNumber(accountNumber));
    }

    /**
     * Get accounts by customer ID with pagination.
     *
     * @param customerId Customer ID to search for
     * @param pageable   Pagination information
     * @return Page of AccountDTO objects
     */
    @GetMapping("/by-customer/{customerId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<AccountDTO>> getAccountsByCustomerId(
            @PathVariable String customerId,
            Pageable pageable) {
        return ResponseEntity.ok(accountService.getAccountsByCustomerId(customerId, pageable));
    }

    /**
     * Get accounts by a list of account numbers with pagination.
     *
     * @param accountNumbers List of account numbers to search for
     * @param pageable       Pagination information
     * @return Page of AccountDTO objects
     */
    @GetMapping("/by-account-numbers")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<AccountDTO>> getAccountsByAccountNumbers(
            @RequestParam List<String> accountNumbers,
            Pageable pageable) {
        return ResponseEntity.ok(accountService.getAccountsByAccountNumbers(accountNumbers, pageable));
    }

    /**
     * Get accounts by description (partial match) with pagination.
     *
     * @param description Description to search for
     * @param pageable    Pagination information
     * @return Page of AccountDTO objects
     */
    @GetMapping("/by-description")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<AccountDTO>> getAccountsByDescription(
            @RequestParam String description,
            Pageable pageable) {
        return ResponseEntity.ok(accountService.getAccountsByDescription(description, pageable));
    }

    /**
     * Update account description.
     * Uses optimistic locking to handle concurrent updates.
     *
     * @param accountNumber    Account number to update
     * @param accountUpdateDTO DTO containing the new description
     * @return Updated AccountDTO
     */
    @PutMapping("/{accountNumber}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AccountDTO> updateAccountDescription(
            @PathVariable String accountNumber,
            @Valid @RequestBody AccountUpdateDTO accountUpdateDTO) {
        return ResponseEntity.ok(accountService.updateAccountDescription(accountNumber, accountUpdateDTO));
    }
}