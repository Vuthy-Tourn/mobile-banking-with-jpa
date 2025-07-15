package com.vuthy.mobilebankingapi.service.Impl;

import com.vuthy.mobilebankingapi.domain.Segment;
import com.vuthy.mobilebankingapi.repository.SegmentRepository;
import com.vuthy.mobilebankingapi.service.SegmentService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                createSegment("Regular", 5000),
                createSegment("Silver", 10000),
                createSegment("Gold", 50000)
        );

        segmentRepository.saveAll(defaultSegments);
    }

    @Override
    public Segment createSegment(String name, Integer overLimit) {
        Segment segment = new Segment();
        segment.setName(name);
        segment.setOverLimit(overLimit);
        segment.setIsDeleted(false);
        return segment;
    }

}
