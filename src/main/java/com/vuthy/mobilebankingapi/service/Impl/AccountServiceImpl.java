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
import com.vuthy.mobilebankingapi.repository.SegmentRepository;
import com.vuthy.mobilebankingapi.service.AccountService;
import com.vuthy.mobilebankingapi.util.CurrencyUtil;
import com.vuthy.mobilebankingapi.util.CustomerSegmentUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CustomerRepository customerRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final SegmentRepository segmentRepository;

    @Override
    public AccountResponse createAccount(CreateAccountRequest createAccountRequest) {
        Account account = accountMapper.toAccount(createAccountRequest);

        Customer customer = customerRepository.findByPhoneNumber(createAccountRequest.phoneNumber())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer phone number is not found"));

        String segment = customer.getCustomerSegment().getName();

        switch (createAccountRequest.actCurrency()){
            case CurrencyUtil.DOLLAR -> {
                if (createAccountRequest.balance().compareTo(BigDecimal.TEN) < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Balance must be greater than ten dollars");
                }

                // segment with dollar
                if (segment.equalsIgnoreCase(CustomerSegmentUtil.GOLD.toString())) {
                    account.setOverLimit(BigDecimal.valueOf(50000));
                } else if (segment.equalsIgnoreCase(CustomerSegmentUtil.SILVER.toString())) {
                    account.setOverLimit(BigDecimal.valueOf(10000));
                } else if (segment.equalsIgnoreCase(CustomerSegmentUtil.REGULAR.toString())) {
                    account.setOverLimit(BigDecimal.valueOf(5000));
                }
            }
            case CurrencyUtil.RIEL -> {
                if (createAccountRequest.balance().compareTo(BigDecimal.valueOf(40000)) < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Balance must be greater than 40000 riel");
                }

                // segment with riel
                if (segment.equalsIgnoreCase(CustomerSegmentUtil.GOLD.toString())) {
                    account.setOverLimit(BigDecimal.valueOf(50000L * 4000));
                } else if (segment.equalsIgnoreCase(CustomerSegmentUtil.SILVER.toString())) {
                    account.setOverLimit(BigDecimal.valueOf(10000 * 4000));
                } else if (segment.equalsIgnoreCase(CustomerSegmentUtil.REGULAR.toString())) {
                    account.setOverLimit(BigDecimal.valueOf(5000 * 4000));
                }
            }
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Currency is not available");
        }

        if(accountRepository.existsByActNo(createAccountRequest.actNo())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account Number is already exists");
        }

        AccountType actType = accountTypeRepository.findByName(createAccountRequest.actType())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Account Type is not found"));

        account.setActType(actType);
        account.setIsDeleted(false);
        account.setIsHide(false);
        account.setActCurrency(createAccountRequest.actCurrency().name());
        account.setReceiverTransactions(new ArrayList<>());
        account.setSenderTransactions(new ArrayList<>());
        account.setCustomer(customer);

        if(createAccountRequest.actNo().isBlank()){
            String actNo;
            do {
                actNo = "ISTAD-" + String.format("%06d", new Random().nextInt(1_000_000)); // Max: 999,999
            } while (accountRepository.existsByActNo(actNo));
            account.setActNo(actNo);
        }

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
