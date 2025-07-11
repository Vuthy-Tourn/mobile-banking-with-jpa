package com.vuthy.mobilebankingapi.service;

import com.vuthy.mobilebankingapi.dto.CreateCustomerRequest;
import com.vuthy.mobilebankingapi.dto.CustomerResponse;
import com.vuthy.mobilebankingapi.dto.UpdateCustomerRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {
    CustomerResponse createCustomer(CreateCustomerRequest createCustomerRequest);
    List<CustomerResponse> findAllCustomers();
    CustomerResponse findCustomerByPhoneNumber(String phoneNumber);
    CustomerResponse updateCustomerByPhoneNumber(String phoneNumber, UpdateCustomerRequest updateCustomerRequest);
    void deleteCustomerByPhoneNumber(String phoneNumber);
}
