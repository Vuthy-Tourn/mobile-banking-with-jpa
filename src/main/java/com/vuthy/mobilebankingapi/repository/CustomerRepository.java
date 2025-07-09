package com.vuthy.mobilebankingapi.repository;

import com.vuthy.mobilebankingapi.domain.Customer;
import com.vuthy.mobilebankingapi.dto.CustomerResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);
}
