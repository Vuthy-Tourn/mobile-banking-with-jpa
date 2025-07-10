package com.vuthy.mobilebankingapi.repository;

import com.vuthy.mobilebankingapi.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);
    Optional<Customer> findByPhoneNumber(String phoneNumber);
}
