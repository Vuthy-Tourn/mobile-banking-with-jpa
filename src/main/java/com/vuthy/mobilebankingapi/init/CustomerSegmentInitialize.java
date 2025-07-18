package com.vuthy.mobilebankingapi.init;

import com.vuthy.mobilebankingapi.domain.CustomerSegment;
import com.vuthy.mobilebankingapi.repository.SegmentRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomerSegmentInitialize {

    private final SegmentRepository segmentRepository;

    @PostConstruct
    @Transactional
    public void initDefaultSegments() {
        if (segmentRepository.count() == 0) {
            List<CustomerSegment> defaultCustomerSegments = Arrays.asList(
                    createSegment("REGULAR", BigDecimal.valueOf(5000)),
                    createSegment("SILVER", BigDecimal.valueOf(10000)),
                    createSegment("GOLD", BigDecimal.valueOf(50000))
            );
            segmentRepository.saveAll(defaultCustomerSegments);
        }
    }

    public CustomerSegment createSegment(String name, BigDecimal overLimit) {
        CustomerSegment customerSegment = new CustomerSegment();
        customerSegment.setName(name);
        customerSegment.setOverLimit(overLimit);
        customerSegment.setIsDeleted(false);
        return customerSegment;
    }
}
