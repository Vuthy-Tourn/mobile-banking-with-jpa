package com.vuthy.mobilebankingapi.dto;

import lombok.Builder;

@Builder
public record CustomerResponse(
        String fullName,
        String gender,
        String email
) {
}
