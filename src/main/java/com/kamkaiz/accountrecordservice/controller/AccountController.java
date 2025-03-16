package com.kamkaiz.accountrecordservice.controller;

import com.kamkaiz.accountrecordservice.dto.AccountDTO;
import com.kamkaiz.accountrecordservice.dto.AccountUpdateDTO;
import com.kamkaiz.accountrecordservice.dto.ApiResponse;
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
 * All endpoints require authentication and support pagination for list operations.
 * 
 * Pagination is handled through Spring's Pageable parameter, which supports:
 * - page: Zero-based page index (0..N)
 * - size: Number of records per page
 * - sort: Sorting criteria in the format: property(,asc|desc)
 *
 * @see org.springframework.data.domain.Pageable
 * @see com.kamkaiz.accountrecordservice.service.AccountService
 * @see com.kamkaiz.accountrecordservice.dto.AccountDTO
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * Get all accounts with pagination.
     *
     * @param pageable Pagination information (page, size, sort)
     * @return Page of AccountDTO objects containing account details
     * @throws org.springframework.security.access.AccessDeniedException if user is not authenticated
     * @see org.springframework.data.domain.Page
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Page<AccountDTO>>> getAllAccounts(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(accountService.getAllAccounts(pageable)));
    }

    /**
     * Get account by account number.
     *
     * @param accountNumber Account number to search for
     * @return AccountDTO object containing account details
     * @throws javax.persistence.EntityNotFoundException if account is not found
     * @throws org.springframework.security.access.AccessDeniedException if user is not authenticated
     */
    @GetMapping("/{accountNumber}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<AccountDTO>> getAccountByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(ApiResponse.success(accountService.getAccountByAccountNumber(accountNumber)));
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
    public ResponseEntity<ApiResponse<Page<AccountDTO>>> getAccountsByCustomerId(
            @PathVariable String customerId,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(accountService.getAccountsByCustomerId(customerId, pageable)));
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
    public ResponseEntity<ApiResponse<Page<AccountDTO>>> getAccountsByAccountNumbers(
            @RequestParam List<String> accountNumbers,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(accountService.getAccountsByAccountNumbers(accountNumbers, pageable)));
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
    public ResponseEntity<ApiResponse<Page<AccountDTO>>> getAccountsByDescription(
            @RequestParam String description,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(accountService.getAccountsByDescription(description, pageable)));
    }

    /**
     * Update account description.
     * Uses optimistic locking to handle concurrent updates.
     * If multiple users attempt to update the same account simultaneously,
     * only one update will succeed while others will receive a conflict error.
     *
     * @param accountNumber Account number to update
     * @param accountUpdateDTO DTO containing the new description
     * @return Updated AccountDTO
     * @throws javax.persistence.EntityNotFoundException if account is not found
     * @throws org.springframework.orm.ObjectOptimisticLockingFailureException if concurrent update detected
     * @throws org.springframework.security.access.AccessDeniedException if user is not authenticated
     * @throws javax.validation.ConstraintViolationException if validation fails
     */
    @PutMapping("/{accountNumber}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<AccountDTO>> updateAccountDescription(
            @PathVariable String accountNumber,
            @Valid @RequestBody AccountUpdateDTO accountUpdateDTO) {
        return ResponseEntity.ok(ApiResponse.success(accountService.updateAccountDescription(accountNumber, accountUpdateDTO), "Account description updated successfully."));
    }
}
