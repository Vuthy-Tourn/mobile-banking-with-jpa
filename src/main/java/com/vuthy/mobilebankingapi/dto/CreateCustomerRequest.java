package com.vuthy.mobilebankingapi.dto;

import com.vuthy.mobilebankingapi.domain.Segment;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateCustomerRequest(
        @NotBlank(message = "Full Name is required")
        String fullName,

        @NotBlank(message = "Gender is required")
        String gender,

        @Email
        String email,
        String phoneNumber,
        String remark,

        @NotBlank(message = "National Card ID is required")
        String nationalCardId,

        @NotBlank(message = "Segment is required")
        String segmentName
) {
}
