package com.kamkaiz.accountrecordservice.batch;

import com.kamkaiz.accountrecordservice.model.Account;
import com.kamkaiz.accountrecordservice.model.AccountTransaction;
import com.kamkaiz.accountrecordservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Batch writer component for processing account transactions.
 * Implements Spring Batch's ItemWriter interface to handle bulk writing of account transactions.
 * Uses transactional processing to ensure data consistency.
 *
 * @see org.springframework.batch.item.ItemWriter
 * @see com.kamkaiz.accountrecordservice.model.AccountTransaction
 * @see com.kamkaiz.accountrecordservice.model.Account
 */
@Component
@RequiredArgsConstructor
public class AccountTransactionWriter implements ItemWriter<AccountTransaction> {

    private final AccountRepository accountRepository;

    /**
     * Writes a batch of account transactions to the database.
     * Converts AccountTransaction objects to Account entities and performs bulk save operation.
     * Transaction is rolled back if any error occurs during the process.
     *
     * @param transactions List of AccountTransaction objects to be processed
     * @throws Exception if any error occurs during the write operation
     */
    @Override
    @Transactional
    public void write(List<? extends AccountTransaction> transactions) throws Exception {
        try {
            List<Account> accounts = new ArrayList<>();
            for (AccountTransaction transaction : transactions) {
                Account account = Account.builder()
                        .accountNumber(transaction.getAccountNumber())
                        .customerId(transaction.getCustomerId())
                        .transactionAmount(transaction.getTransactionAmount())
                        .description(transaction.getDescription())
                        .transactionDate(transaction.getTransactionDate())
                        .transactionTime(transaction.getTransactionTime())
                        .build();
                accounts.add(account);
            }
            accountRepository.saveAll(accounts);
        } catch (Exception e) {
            throw new Exception("Error writing transactions: " + e.getMessage(), e);
        }
    }
}