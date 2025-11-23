package com.idea101.backendengine.customer.service.impl;

import com.idea101.backendengine.common.exception.DuplicateAccountCreation;
import com.idea101.backendengine.customer.dto.CreateAccountDto;
import com.idea101.backendengine.customer.entity.CustomerAccount;
import com.idea101.backendengine.customer.repository.CustomerAccountRepository;
import com.idea101.backendengine.customer.service.CustomerAccountService;
import com.idea101.backendengine.customer.service.DeviceRegService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerAccountImpl implements CustomerAccountService {

    private final CustomerAccountRepository accountRepository;
    private final DeviceRegService deviceRegService;

    @Override
    public void createCustomerAccount(CreateAccountDto dto) {
        accountRepository.getCustomerAccountByIdOrEmailOrPhoneNumber(
                dto.getId(), dto.getEmail(), dto.getPhoneNumber()
        ).ifPresent(account -> {
            throw new DuplicateAccountCreation("Customer already exists with given id, email, or phone number");
        });

        CustomerAccount account = new CustomerAccount();
        account.setId(dto.getId());
        account.setName(dto.getName());
        account.setEmail(dto.getEmail());
        account.setPhoneNumber(dto.getPhoneNumber());
        account.setDob(dto.getDob());
        account.setSex(dto.getSex());
        accountRepository.save(account);

        if(dto.getDeviceId() != null && dto.getFcmToken() != null) {
            deviceRegService.registerDevice(account.getId(),dto.getDeviceId(), dto.getPlatform(), dto.getFcmToken());
        }
    }
}
