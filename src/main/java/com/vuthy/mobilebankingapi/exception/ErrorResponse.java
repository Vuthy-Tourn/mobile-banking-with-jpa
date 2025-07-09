package com.vuthy.mobilebankingapi.exception;

import lombok.Builder;

import java.time.LocalDate;
@Builder
public record ErrorResponse<T>(
        String message,
        LocalDate timestamp,
        Integer status,
        T details
) {
}
