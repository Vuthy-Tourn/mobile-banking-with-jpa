package com.vuthy.mobilebankingapi.service;

import com.vuthy.mobilebankingapi.dto.AccountResponse;
import com.vuthy.mobilebankingapi.dto.CreateAccountRequest;
import com.vuthy.mobilebankingapi.dto.UpdateAccountRequest;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(CreateAccountRequest createAccountRequest);
    List<AccountResponse> getAllAccounts();
    AccountResponse getAccountByActNo(String actNo);
    AccountResponse getAccountsByCustomer(String phoneNumber);
    AccountResponse updateAccountByActNo(String actNo, UpdateAccountRequest updateAccountRequest);
    void deleteAccountByActNo(String actNo);
    void disableByActNo(String actNo);
}
