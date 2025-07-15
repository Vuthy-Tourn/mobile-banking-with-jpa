package com.vuthy.mobilebankingapi.service.Impl;

import com.vuthy.mobilebankingapi.domain.Segment;
import com.vuthy.mobilebankingapi.repository.SegmentRepository;
import com.vuthy.mobilebankingapi.service.SegmentService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SegmentServiceImpl implements SegmentService {

    private final SegmentRepository segmentRepository;

    @PostConstruct
    @Transactional
    public void initDefaultSegments() {
        List<Segment> defaultSegments = Arrays.asList(
                createSegment("Regular", BigDecimal.valueOf(5000)),
                createSegment("Silver", BigDecimal.valueOf(10000)),
                createSegment("Gold", BigDecimal.valueOf(50000))
        );

        segmentRepository.saveAll(defaultSegments);
    }

    @Override
    public Segment createSegment(String name, BigDecimal overLimit) {
        Segment segment = new Segment();
        segment.setName(name);
        segment.setOverLimit(overLimit);
        segment.setIsDeleted(false);
        return segment;
    }

}
