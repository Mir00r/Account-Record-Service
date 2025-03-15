package com.kamkaiz.accountrecordservice.batch;

import com.kamkaiz.accountrecordservice.model.AccountTransaction;
import org.springframework.batch.item.ItemProcessor;

public class AccountTransactionProcessor implements ItemProcessor<AccountTransaction, AccountTransaction> {

    @Override
    public AccountTransaction process(AccountTransaction transaction) throws Exception {
        // Here you can add validation, transformation, or filtering logic
        // For now, we'll just pass through the transaction
        return transaction;
    }
}