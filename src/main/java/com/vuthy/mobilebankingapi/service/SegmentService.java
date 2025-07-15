package com.vuthy.mobilebankingapi.service;

import com.vuthy.mobilebankingapi.domain.Segment;

import java.math.BigDecimal;

public interface SegmentService {
    Segment createSegment(String name, BigDecimal overLimit);
}
