package com.vuthy.mobilebankingapi.dto;

import com.vuthy.mobilebankingapi.domain.AccountType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountResponse(
        String actNo,
        String actType,
        String actCurrency,
        BigDecimal balance,
        Boolean isDeleted,
        BigDecimal overLimit
) {
}
