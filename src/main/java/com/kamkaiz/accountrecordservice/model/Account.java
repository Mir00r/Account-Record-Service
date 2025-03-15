package com.kamkaiz.accountrecordservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account extends BaseEntity {

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false)
    private BigDecimal transactionAmount;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @Column(nullable = false)
    private LocalDateTime transactionTime;
}