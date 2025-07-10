package com.vuthy.mobilebankingapi.service;

import com.vuthy.mobilebankingapi.dto.CreateCustomerRequest;
import com.vuthy.mobilebankingapi.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse createCustomer(CreateCustomerRequest createCustomerRequest);
    List<CustomerResponse> findAllCustomers();
    CustomerResponse findCustomerByPhoneNumber(String phoneNumber);
}
