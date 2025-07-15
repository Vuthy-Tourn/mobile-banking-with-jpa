package com.vuthy.mobilebankingapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAccountRequest(
        @NotBlank(message = "Account number is required")
        String actNo,

        @NotBlank(message = "Currency is required")
        String actCurrency,

        @NotNull(message = "Customer Phone Number is required")
        String phoneNumber
) {
}
