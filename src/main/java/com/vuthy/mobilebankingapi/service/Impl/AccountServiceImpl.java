package com.vuthy.mobilebankingapi.service.Impl;

import com.vuthy.mobilebankingapi.domain.Account;
import com.vuthy.mobilebankingapi.domain.AccountType;
import com.vuthy.mobilebankingapi.domain.Customer;
import com.vuthy.mobilebankingapi.dto.AccountResponse;
import com.vuthy.mobilebankingapi.dto.CreateAccountRequest;
import com.vuthy.mobilebankingapi.dto.UpdateAccountRequest;
import com.vuthy.mobilebankingapi.mapper.AccountMapper;
import com.vuthy.mobilebankingapi.repository.AccountRepository;
import com.vuthy.mobilebankingapi.repository.AccountTypeRepository;
import com.vuthy.mobilebankingapi.repository.CustomerRepository;
import com.vuthy.mobilebankingapi.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CustomerRepository customerRepository;
    private final AccountTypeRepository accountTypeRepository;

    @Override
    public AccountResponse createAccount(CreateAccountRequest createAccountRequest) {

        if(accountRepository.existsByActNo(createAccountRequest.actNo())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account Number is already exists");
        }

        Customer customer = customerRepository.findByPhoneNumber(createAccountRequest.phoneNumber())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        AccountType savingsType = accountTypeRepository.findByName("Savings")
                .orElseGet(() -> {
                    AccountType newType = new AccountType();
                    newType.setName("Savings");
                    return accountTypeRepository.save(newType);
                });

        Account account = accountMapper.toAccount(createAccountRequest);
        account.setActType(savingsType);
        account.setIsDeleted(false);
        account.setBalance(BigDecimal.valueOf(0));
        account.setReceiverTransactions(new ArrayList<>());
        account.setSenderTransactions(new ArrayList<>());
        account.setCustomer(customer);

        accountRepository.save(account);

        return accountMapper.fromAccount(account);
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        List<AccountResponse> accounts = accountRepository.findAll()
                .stream()
                .map(accountMapper::fromAccount)
                .toList();
        return accounts;
    }

    @Override
    public AccountResponse getAccountByActNo(String actNo) {
        return accountRepository.findAccountByActNo(actNo)
                .map(accountMapper::fromAccount)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account number is not found"));
    }

    @Override
    public AccountResponse getAccountsByCustomer(String phoneNumber) {
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer is not found with phone number"));
        AccountResponse accounts = accountRepository.findAccountByCustomer(customer)
                .map(accountMapper::fromAccount)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account is not found"));
        return accounts;
    }

    @Override
    public AccountResponse updateAccountByActNo(String actNo, UpdateAccountRequest updateAccountRequest) {
        Account account = accountRepository.findAccountByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account is not found"));
        accountMapper.toAccountPartially(updateAccountRequest, account);
        accountRepository.save(account);
        return accountMapper.fromAccount(account);
    }

    @Override
    public void deleteAccountByActNo(String actNo) {
        Account account = accountRepository.findAccountByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account is not found"));
        accountRepository.delete(account);
    }

    @Transactional
    @Override
    public void disableByActNo(String actNo) {
        Account account = accountRepository.findAccountByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account is not found"));
        if(account.getIsDeleted()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account is already disabled");
        }
        account.setIsDeleted(true);
        accountRepository.save(account);
    }
}
