package com.kamkaiz.accountrecordservice.mapper;

import com.kamkaiz.accountrecordservice.dto.AccountDTO;
import com.kamkaiz.accountrecordservice.model.Account;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between Account entities and DTOs.
 * Following Single Responsibility Principle by separating mapping logic from business logic.
 */
@Component
public class AccountMapper {

    /**
     * Maps an Account entity to an AccountDTO.
     *
     * @param account Account entity to be mapped
     * @return AccountDTO containing the mapped data
     */
    public AccountDTO toDTO(Account account) {
        if (account == null) {
            return null;
        }
        
        return AccountDTO.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .customerId(account.getCustomerId())
                .balance(account.getBalance())
                .description(account.getDescription())
                .build();
    }

    /**
     * Maps an AccountDTO to an Account entity.
     *
     * @param accountDTO AccountDTO to be mapped
     * @return Account entity containing the mapped data
     */
    public Account toEntity(AccountDTO accountDTO) {
        if (accountDTO == null) {
            return null;
        }
        
        Account account = new Account();
        account.setId(accountDTO.getId());
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setCustomerId(accountDTO.getCustomerId());
        account.setBalance(accountDTO.getBalance());
        account.setDescription(accountDTO.getDescription());
        return account;
    }
}