package com.vuthy.mobilebankingapi.mapper;

import com.vuthy.mobilebankingapi.domain.Customer;
import com.vuthy.mobilebankingapi.dto.CreateCustomerRequest;
import com.vuthy.mobilebankingapi.dto.CustomerResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    // dto -> model
    // model -> dto
    // return type is converted data
    // parameter is source data

    CustomerResponse fromCustomer(Customer customer);

    Customer toCustomer(CreateCustomerRequest createCustomerRequest);
}
