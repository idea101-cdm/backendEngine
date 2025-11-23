package com.idea101.backendengine.customer.service;

import com.idea101.backendengine.customer.dto.CreateAccountDto;

import java.util.UUID;

public interface CustomerAccountService {
    void createCustomerAccount(CreateAccountDto createAccountDto);
}
