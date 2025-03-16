package com.kamkaiz.accountrecordservice.config;

import com.kamkaiz.accountrecordservice.model.Account;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Custom implementation of FieldSetMapper for mapping CSV file data to Account entities.
 * This class handles the conversion of raw string data from CSV files into strongly-typed
 * Account objects, including proper parsing of dates, times, and numeric values.
 */
public class CustomAccountFieldSetMapper implements FieldSetMapper<Account> {

    /**
     * Formatter for parsing date strings in the format "yyyy-MM-dd"
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Formatter for parsing time strings in the format "HH:mm:ss"
     */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Maps a set of fields from the CSV file to an Account object.
     * 
     * @param fieldSet The set of fields read from the CSV file
     * @return A fully populated Account object
     * @throws BindException if there are any errors in binding the fields
     */
    @Override
    public Account mapFieldSet(FieldSet fieldSet) throws BindException {
        // Build the basic account object with string and numeric fields
        Account account = Account.builder()
                .accountNumber(fieldSet.readString("accountNumber"))
                .transactionAmount(new BigDecimal(fieldSet.readString("transactionAmount")))
                .description(fieldSet.readString("description"))
                .customerId(fieldSet.readString("customerId"))
                .build();

        // Parse date and time strings using the defined formatters
        LocalDate date = LocalDate.parse(fieldSet.readString("transactionDate"), DATE_FORMATTER);
        LocalTime time = LocalTime.parse(fieldSet.readString("transactionTime"), TIME_FORMATTER);
        
        // Combine date and time into a single LocalDateTime object
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        account.setTransactionDate(dateTime);
        account.setTransactionTime(dateTime);
        
        // Initialize the balance with the transaction amount
        account.setBalance(account.getTransactionAmount());

        return account;
    }
}