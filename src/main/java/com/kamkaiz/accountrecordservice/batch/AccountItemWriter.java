package com.kamkaiz.accountrecordservice.batch;

import com.kamkaiz.accountrecordservice.model.Account;
import com.kamkaiz.accountrecordservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A Spring Batch writer component that handles persisting Account records to the database.
 * This class implements Spring Batch's ItemWriter interface to save processed Account objects
 * in batches for optimal performance.
 *
 * Key responsibilities:
 * - Receives batches of processed Account objects
 * - Persists Account records to the database efficiently using batch operations
 * - Logs writing activities for monitoring and debugging
 * - Handles database operations within Spring's transaction management
 *
 * The writer is the final component in the Spring Batch chunk processing chain:
 * Reader -> Processor -> Writer
 *
 * @see org.springframework.batch.item.ItemWriter
 * @see com.kamkaiz.accountrecordservice.model.Account
 * @see com.kamkaiz.accountrecordservice.repository.AccountRepository
 */
@Component
@RequiredArgsConstructor
public class AccountItemWriter implements ItemWriter<Account> {

    private static final Logger log = LoggerFactory.getLogger(AccountItemWriter.class);
    private final AccountRepository accountRepository;

    /**
     * Writes a batch of Account objects to the database.
     * This method is called by Spring Batch after a chunk of items has been processed.
     * It uses the AccountRepository to perform batch inserts/updates efficiently.
     *
     * @param accounts The list of Account objects to be written to the database
     */
    @Override
    public void write(List<? extends Account> accounts) {
        log.info("Writing {} accounts to the database", accounts.size());
        accountRepository.saveAll(accounts);
    }
}