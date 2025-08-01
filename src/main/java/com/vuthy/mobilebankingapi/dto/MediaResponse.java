package com.vuthy.mobilebankingapi.dto;

import lombok.Builder;

@Builder
public record MediaResponse(
    String name,
    String mimeType,
    String uri,
    Long size
) {
}
