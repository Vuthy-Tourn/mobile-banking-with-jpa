package com.vuthy.mobilebankingapi.service;

import com.vuthy.mobilebankingapi.domain.Segment;

public interface SegmentService {
    Segment createSegment(String name, Integer overLimit);
}
