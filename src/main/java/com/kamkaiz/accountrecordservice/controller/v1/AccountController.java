package com.kamkaiz.accountrecordservice.controller.v1;

import com.kamkaiz.accountrecordservice.dto.AccountDTO;
import com.kamkaiz.accountrecordservice.dto.AccountUpdateDTO;
import com.kamkaiz.accountrecordservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Account resources (API v1).
 * Implements Richardson Maturity Model Level 3 (HATEOAS)
 */
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * Get all accounts with pagination and HATEOAS links.
     *
     * @param pageable Pagination information
     * @return CollectionModel of AccountDTO with HATEOAS links
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CollectionModel<EntityModel<AccountDTO>>> getAllAccounts(Pageable pageable) {
        Page<AccountDTO> accounts = accountService.getAllAccounts(pageable);
        List<EntityModel<AccountDTO>> accountResources = accounts.getContent().stream()
                .map(this::toAccountResource)
                .collect(Collectors.toList());

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AccountController.class)
                .getAllAccounts(pageable)).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(accountResources, selfLink));
    }

    /**
     * Get account by account number with HATEOAS links.
     *
     * @param accountNumber Account number to search for
     * @return EntityModel of AccountDTO with HATEOAS links
     */
    @GetMapping("/{accountNumber}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EntityModel<AccountDTO>> getAccountByAccountNumber(@PathVariable String accountNumber) {
        AccountDTO account = accountService.getAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(toAccountResource(account));
    }

    /**
     * Get accounts by customer ID with pagination and HATEOAS links.
     *
     * @param customerId Customer ID to search for
     * @param pageable   Pagination information
     * @return CollectionModel of AccountDTO with HATEOAS links
     */
    @GetMapping("/by-customer/{customerId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CollectionModel<EntityModel<AccountDTO>>> getAccountsByCustomerId(
            @PathVariable String customerId,
            Pageable pageable) {
        Page<AccountDTO> accounts = accountService.getAccountsByCustomerId(customerId, pageable);
        List<EntityModel<AccountDTO>> accountResources = accounts.getContent().stream()
                .map(this::toAccountResource)
                .collect(Collectors.toList());

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AccountController.class)
                .getAccountsByCustomerId(customerId, pageable)).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(accountResources, selfLink));
    }

    /**
     * Get accounts by account numbers with pagination and HATEOAS links.
     *
     * @param accountNumbers List of account numbers to search for
     * @param pageable       Pagination information
     * @return CollectionModel of AccountDTO with HATEOAS links
     */
    @GetMapping("/by-account-numbers")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CollectionModel<EntityModel<AccountDTO>>> getAccountsByAccountNumbers(
            @RequestParam List<String> accountNumbers,
            Pageable pageable) {
        Page<AccountDTO> accounts = accountService.getAccountsByAccountNumbers(accountNumbers, pageable);
        List<EntityModel<AccountDTO>> accountResources = accounts.getContent().stream()
                .map(this::toAccountResource)
                .collect(Collectors.toList());

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AccountController.class)
                .getAccountsByAccountNumbers(accountNumbers, pageable)).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(accountResources, selfLink));
    }

    /**
     * Get accounts by description with pagination and HATEOAS links.
     *
     * @param description Description to search for
     * @param pageable    Pagination information
     * @return CollectionModel of AccountDTO with HATEOAS links
     */
    @GetMapping("/by-description")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CollectionModel<EntityModel<AccountDTO>>> getAccountsByDescription(
            @RequestParam String description,
            Pageable pageable) {
        Page<AccountDTO> accounts = accountService.getAccountsByDescription(description, pageable);
        List<EntityModel<AccountDTO>> accountResources = accounts.getContent().stream()
                .map(this::toAccountResource)
                .collect(Collectors.toList());

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AccountController.class)
                .getAccountsByDescription(description, pageable)).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(accountResources, selfLink));
    }

    /**
     * Update account description.
     * Uses optimistic locking to handle concurrent updates.
     *
     * @param accountNumber    Account number to update
     * @param accountUpdateDTO DTO containing the new description
     * @return EntityModel of updated AccountDTO with HATEOAS links
     */
    @PutMapping("/{accountNumber}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EntityModel<AccountDTO>> updateAccountDescription(
            @PathVariable String accountNumber,
            @Valid @RequestBody AccountUpdateDTO accountUpdateDTO) {
        AccountDTO updatedAccount = accountService.updateAccountDescription(accountNumber, accountUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(toAccountResource(updatedAccount));
    }

    /**
     * Convert AccountDTO to EntityModel with HATEOAS links.
     *
     * @param account AccountDTO to convert
     * @return EntityModel of AccountDTO with HATEOAS links
     */
    private EntityModel<AccountDTO> toAccountResource(AccountDTO account) {
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AccountController.class)
                .getAccountByAccountNumber(account.getAccountNumber())).withSelfRel();
        Link updateLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AccountController.class)
                .updateAccountDescription(account.getAccountNumber(), null)).withRel("update");
        Link customerLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AccountController.class)
                .getAccountsByCustomerId(account.getCustomerId(), null)).withRel("customer-accounts");

        return EntityModel.of(account, selfLink, updateLink, customerLink);
    }
}