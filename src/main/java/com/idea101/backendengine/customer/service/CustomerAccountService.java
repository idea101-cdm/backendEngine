package com.idea101.backendengine.customer.service;

import com.idea101.backendengine.customer.dto.CustomerAccountRequestDto;
import com.idea101.backendengine.customer.dto.CustomerAccountResponseDto;

import java.util.UUID;

public interface CustomerAccountService {
    void createCustomerAccount(CustomerAccountRequestDto accountRequestDto);
    CustomerAccountResponseDto getCustomerAccount(UUID id);
    void updateCustomerAccount(String phoneNumber);
//    void updateCustomerAccount(String email);
}
