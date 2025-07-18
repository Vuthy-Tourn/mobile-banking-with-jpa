package com.vuthy.mobilebankingapi.mapper;

import com.vuthy.mobilebankingapi.domain.Customer;
import com.vuthy.mobilebankingapi.dto.CreateCustomerRequest;
import com.vuthy.mobilebankingapi.dto.CustomerResponse;
import com.vuthy.mobilebankingapi.dto.UpdateCustomerRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
            void toCustomerPartially(UpdateCustomerRequest updateCustomerRequest,
                                     @MappingTarget Customer customer);

    // dto -> model
    // model -> dto
    // return type is converted | target data
    // parameter is source data

    CustomerResponse fromCustomer(Customer customer);

    @Mapping(source = "customerSegment" ,target= "customerSegment.name" , ignore = true )
    Customer toCustomer(CreateCustomerRequest createCustomerRequest);
}
