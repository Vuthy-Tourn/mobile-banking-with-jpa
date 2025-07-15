package com.vuthy.mobilebankingapi.controller;

import com.vuthy.mobilebankingapi.dto.CreateCustomerRequest;
import com.vuthy.mobilebankingapi.dto.CustomerResponse;
import com.vuthy.mobilebankingapi.dto.UpdateCustomerRequest;
import com.vuthy.mobilebankingapi.service.CustomerService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/{phoneNumber}")
    public CustomerResponse getCustomerByPhoneNumber(@PathVariable String phoneNumber) {
        return customerService.findCustomerByPhoneNumber(phoneNumber);
    }

    @GetMapping
    public List<CustomerResponse> getAllCustomers() {
        return customerService.findAllCustomers();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CustomerResponse createCustomer(@Valid @RequestBody CreateCustomerRequest createCustomerRequest) {
        return customerService.createCustomer(createCustomerRequest);

    }

    @PatchMapping("/{phoneNumber}")
    public CustomerResponse updateCustomer(@PathVariable String phoneNumber, @RequestBody UpdateCustomerRequest updateCustomerRequest) {
        return customerService.updateCustomerByPhoneNumber(phoneNumber, updateCustomerRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{phoneNumber}")
    public void deleteCustomer(@PathVariable String phoneNumber) {
        customerService.deleteCustomerByPhoneNumber(phoneNumber);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{phoneNumber}")
    public void disableCustomerByPhoneNumber(@PathVariable String phoneNumber) {
        customerService.disableCustomerByPhoneNumber(phoneNumber);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/verify/{nationalCardId}")
    public void verifyKYCByNationalCardId(@PathVariable String nationalCardId) {
        customerService.verifyKYCByNationalCardId(nationalCardId);
    }

}
