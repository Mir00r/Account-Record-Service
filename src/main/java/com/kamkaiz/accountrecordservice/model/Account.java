package com.kamkaiz.accountrecordservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing a financial account transaction record.
 * This class extends BaseEntity to inherit common fields like id and audit information.
 * It stores essential transaction details including account number, customer information,
 * transaction amounts, and timing details.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account extends BaseEntity {

    /**
     * Unique identifier for the account involved in the transaction
     */
    @Column(nullable = false)
    private String accountNumber;

    /**
     * Unique identifier for the customer who owns the account
     */
    @Column(nullable = false)
    private String customerId;

    /**
     * The amount involved in this specific transaction
     */
    @Column(nullable = false)
    private BigDecimal transactionAmount;

    /**
     * Current balance of the account after this transaction
     */
    @Column(nullable = false)
    private BigDecimal balance;

    /**
     * Detailed description of the transaction
     */
    @Column(length = 1000)
    private String description;

    /**
     * Date when the transaction occurred
     */
    @Column(nullable = false)
    private LocalDateTime transactionDate;

    /**
     * Time when the transaction was processed
     */
    @Column(nullable = false)
    private LocalDateTime transactionTime;
}