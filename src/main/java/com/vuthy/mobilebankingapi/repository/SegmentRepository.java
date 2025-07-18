package com.vuthy.mobilebankingapi.repository;

import com.vuthy.mobilebankingapi.domain.CustomerSegment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SegmentRepository extends JpaRepository<CustomerSegment, Integer> {
    Optional<CustomerSegment> getSegmentByName(String name);
}
