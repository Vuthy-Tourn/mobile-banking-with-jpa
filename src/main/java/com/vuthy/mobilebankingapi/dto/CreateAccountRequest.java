package com.vuthy.mobilebankingapi.dto;

import com.vuthy.mobilebankingapi.util.CurrencyUtil;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateAccountRequest(
        String actNo,

        @NotBlank(message = "Account name is required")
        String actName,

        CurrencyUtil actCurrency,

        BigDecimal balance,

        @NotNull(message = "Customer Phone Number is required")
        String phoneNumber,

        String actType
) {
}
