package com.vuthy.mobilebankingapi.dto;

import java.math.BigDecimal;

public record UpdateAccountRequest(
        BigDecimal balance
) {
}
