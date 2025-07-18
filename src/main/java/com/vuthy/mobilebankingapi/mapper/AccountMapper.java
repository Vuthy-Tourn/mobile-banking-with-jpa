package com.vuthy.mobilebankingapi.mapper;


import com.vuthy.mobilebankingapi.domain.Account;
import com.vuthy.mobilebankingapi.dto.AccountResponse;
import com.vuthy.mobilebankingapi.dto.CreateAccountRequest;
import com.vuthy.mobilebankingapi.dto.UpdateAccountRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toAccountPartially(UpdateAccountRequest updateAccountRequest,
                            @MappingTarget Account account);

    @Mapping(target = "actType", ignore = true)
    Account toAccount(CreateAccountRequest createAccountRequest);

    @Mapping(target = "actType", expression = "java(account.getActType().getName().toUpperCase())")
    AccountResponse fromAccount(Account account);
}
