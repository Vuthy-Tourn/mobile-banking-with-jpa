package com.vuthy.mobilebankingapi.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateCustomerRequest(
        String fullName,

        String gender,

        String remark
) {
}
