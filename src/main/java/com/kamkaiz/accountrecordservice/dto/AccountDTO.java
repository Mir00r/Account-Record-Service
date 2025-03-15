package com.kamkaiz.accountrecordservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for Account entities.
 * This class represents the data structure used for transferring account information
 * between the client and server, providing validation constraints for data integrity.
 *
 * Key features:
 * - Includes all necessary account fields with appropriate validation
 * - Uses Lombok annotations for reducing boilerplate code
 * - Implements validation constraints for data integrity
 *
 * @see com.kamkaiz.accountrecordservice.model.Account
 * @see javax.validation.constraints
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    
    /**
     * The unique identifier for the account.
     * This field is system-generated and not required for creation requests.
     */
    private Long id;
    
    /**
     * The unique account number for the account.
     * This field is required and must not be blank.
     */
    @NotBlank(message = "Account number is required")
    private String accountNumber;
    
    /**
     * The unique identifier of the customer who owns this account.
     * This field is required and must not be blank.
     */
    @NotBlank(message = "Customer ID is required")
    private String customerId;
    
    /**
     * The current balance of the account.
     * This field is required and must not be null.
     * Uses BigDecimal for precise decimal arithmetic.
     */
    @NotNull(message = "Balance is required")
    private BigDecimal balance;
    
    /**
     * Optional description or notes about the account.
     * Limited to 1000 characters maximum.
     */
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
}