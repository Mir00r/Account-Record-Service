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

@Component
@RequiredArgsConstructor
public class AccountTransactionWriter implements ItemWriter<AccountTransaction> {

    private final AccountRepository accountRepository;

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