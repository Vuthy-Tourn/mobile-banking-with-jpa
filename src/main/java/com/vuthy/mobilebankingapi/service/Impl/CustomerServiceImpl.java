package com.vuthy.mobilebankingapi.service.Impl;

import com.vuthy.mobilebankingapi.domain.Customer;
import com.vuthy.mobilebankingapi.domain.KYC;
import com.vuthy.mobilebankingapi.domain.Segment;
import com.vuthy.mobilebankingapi.dto.CreateCustomerRequest;
import com.vuthy.mobilebankingapi.dto.CustomerResponse;
import com.vuthy.mobilebankingapi.dto.UpdateCustomerRequest;
import com.vuthy.mobilebankingapi.mapper.CustomerMapper;
import com.vuthy.mobilebankingapi.repository.CustomerRepository;
import com.vuthy.mobilebankingapi.repository.KYCRepository;
import com.vuthy.mobilebankingapi.repository.SegmentRepository;
import com.vuthy.mobilebankingapi.service.CustomerService;
import jakarta.transaction.Transactional;
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
    private final CustomerMapper customerMapper;
    private final KYCRepository kycRepository;
    private final SegmentRepository segmentRepository;

    @Transactional
    @Override
    public void disableCustomerByPhoneNumber(String phoneNumber) {
        if(!customerRepository.existsByPhoneNumber(phoneNumber)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Phone number not found");
        }
        customerRepository.disableByPhoneNumber(phoneNumber);
    }


    @Override
    public CustomerResponse findCustomerByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumberAndIsDeletedFalse(phoneNumber)
                .map(customerMapper::fromCustomer)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer phone number is not found"));
    }

    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest createCustomerRequest) {

        // validation
        if(customerRepository.existsByEmail(createCustomerRequest.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        if(customerRepository.existsByPhoneNumber(createCustomerRequest.phoneNumber())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exists");
        }

        if(kycRepository.existsByNationalCardId(createCustomerRequest.nationalCardId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "National card already exists");
        }


        // Find segment
        Segment segment = segmentRepository.getSegmentByName(createCustomerRequest.segmentName())
                .orElseThrow(() -> new RuntimeException("Segment not found"));

        Customer customer = customerMapper.toCustomer(createCustomerRequest);
        customer.setIsDeleted(false);
        customer.setAccounts(new ArrayList<>());
        customer.setSegment(segment);

        Customer savedCustomer = customerRepository.save(customer);

        KYC kyc = new KYC();
        kyc.setNationalCardId(createCustomerRequest.nationalCardId());
//        kyc.setIsVerified(false);
//        kyc.setIsDeleted(false);
        kyc.setCustomer(savedCustomer);
        kycRepository.save(kyc);

        savedCustomer.setKyc(kyc);

        log.info("Creating Customer getID before save: " + customer.getId());

        customerRepository.save(savedCustomer);

        log.info("Created Customer getID after save : {}", customer.getId());

        return customerMapper.fromCustomer(savedCustomer);
    }

    @Override
    public List<CustomerResponse> findAllCustomers() {
        List<CustomerResponse> customers = customerRepository.findAllByIsDeletedFalse()
                .stream()
                .map(customerMapper::fromCustomer)
                .toList();

        return customers;
    }

    @Override
    public CustomerResponse updateCustomerByPhoneNumber(String phoneNumber, UpdateCustomerRequest updateCustomerRequest) {

        Customer customer = customerRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer phone number is not found"));

        customerMapper.toCustomerPartially(updateCustomerRequest, customer);

        customer = customerRepository.save(customer);

        return customerMapper.fromCustomer(customer);
    }

    @Override
    public void deleteCustomerByPhoneNumber(String phoneNumber) {
        Customer customer = customerRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer phone number is not found"));
        customerRepository.delete(customer);
    }

    @Override
    public void verifyKYCByNationalCardId(String nationalCardId) {
        if(!kycRepository.existsByNationalCardId(nationalCardId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "National card does not exist");
        }
        KYC kyc = kycRepository.findByNationalCardId(nationalCardId)
                .orElseThrow(() -> new RuntimeException("KYC not found"));
        kyc.setIsVerified(true);
        kycRepository.save(kyc);
    }
}
