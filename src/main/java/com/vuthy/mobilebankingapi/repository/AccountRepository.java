package com.vuthy.mobilebankingapi.repository;

import com.vuthy.mobilebankingapi.domain.Account;
import com.vuthy.mobilebankingapi.domain.AccountType;
import com.vuthy.mobilebankingapi.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Integer> {
    Boolean existsByActNo(String actNo);
    Boolean existsByActType (AccountType actType);
    Optional<Account> findAccountByActNo(String actNo);
    Optional<Account> findAccountByCustomer(Customer customer);
}
