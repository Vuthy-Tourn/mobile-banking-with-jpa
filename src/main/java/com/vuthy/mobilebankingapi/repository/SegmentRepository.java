package com.vuthy.mobilebankingapi.repository;

import com.vuthy.mobilebankingapi.domain.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface SegmentRepository extends JpaRepository<Segment, Integer> {
    Optional<Segment> getSegmentByName(String name);
}
