package com.vuthy.mobilebankingapi.service.Impl;

import com.vuthy.mobilebankingapi.domain.Customer;
import com.vuthy.mobilebankingapi.dto.CreateCustomerRequest;
import com.vuthy.mobilebankingapi.dto.CustomerResponse;
import com.vuthy.mobilebankingapi.repository.CustomerRepository;
import com.vuthy.mobilebankingapi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest createCustomerRequest) {

        // validation
        if(customerRepository.existsByEmail(createCustomerRequest.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        if(customerRepository.existsByPhoneNumber(createCustomerRequest.phoneNumber())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exists");
        }


        Customer customer = new Customer();
        customer.setFullName(createCustomerRequest.fullName());
        customer.setGender(createCustomerRequest.gender());
        customer.setIsDeleted(false);
        customer.setEmail(createCustomerRequest.email());
        customer.setRemark(createCustomerRequest.remark());
        customer.setPhoneNumber(createCustomerRequest.phoneNumber());
        customer.setAccounts(new ArrayList<>());

        log.info("Creating Customer getID before save: " + customer.getId());

        customerRepository.save(customer);

        log.info("Created Customer getID after save : {}", customer.getId());

        return CustomerResponse.builder()
                .fullName(customer.getFullName())
                .gender(customer.getGender())
                .email(customer.getEmail())
                .build();
    }

    @Override
    public List<CustomerResponse> findAllCustomers() {
        List<CustomerResponse> customers = customerRepository.findAll()
                .stream()
                .map(c-> CustomerResponse.builder()
                        .fullName(c.getFullName())
                        .gender(c.getGender())
                        .email(c.getEmail())
                        .build())
                .toList();

        return customers;
    }


}
