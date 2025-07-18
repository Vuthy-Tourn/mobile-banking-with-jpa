package com.vuthy.mobilebankingapi.init;

import com.vuthy.mobilebankingapi.domain.AccountType;
import com.vuthy.mobilebankingapi.repository.AccountTypeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountTypeInitialize {

    private final AccountTypeRepository accountTypeRepository;

    @PostConstruct
    public void init() {
        if(accountTypeRepository.count() == 0) {
            AccountType saving = new AccountType();
            saving.setName("SAVING");

            AccountType payroll = new AccountType();
            payroll.setName("PAYROLL");

            AccountType junior = new AccountType();
            junior.setName("JUNIOR");

            accountTypeRepository.saveAll(List.of(saving, payroll, junior));
        }

    }
}
