package com.vuthy.mobilebankingapi.repository;

import com.vuthy.mobilebankingapi.domain.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AccountTypeRepository extends JpaRepository<AccountType, Integer> {
   Optional<AccountType> findByName(String name);
}
