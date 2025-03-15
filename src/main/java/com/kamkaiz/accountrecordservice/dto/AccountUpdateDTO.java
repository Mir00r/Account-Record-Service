package com.kamkaiz.accountrecordservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateDTO {
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
}