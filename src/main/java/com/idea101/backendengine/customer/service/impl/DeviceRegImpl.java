package com.idea101.backendengine.customer.service.impl;

import com.idea101.backendengine.common.enums.Platform;
import com.idea101.backendengine.common.exception.AccountNotFoundException;
import com.idea101.backendengine.common.exception.DuplicateAccountCreation;
import com.idea101.backendengine.customer.entity.CustomerAccount;
import com.idea101.backendengine.customer.entity.DeviceRegistration;
import com.idea101.backendengine.customer.repository.CustomerAccountRepository;
import com.idea101.backendengine.customer.repository.DeviceRegRepository;
import com.idea101.backendengine.customer.service.DeviceRegService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceRegImpl implements DeviceRegService {

    private final CustomerAccountRepository customerAccountRepository;
    private final DeviceRegRepository deviceRegRepository;

    @Override
    public void registerDevice(UUID customerId, String deviceId, Platform platform, String fcmToken) {
        CustomerAccount customerAccount = customerAccountRepository.findById(customerId).
                orElseThrow(() -> new AccountNotFoundException("Customer Account Not Found for id :"+customerId));
        deviceRegRepository.findByDeviceIdAndCustomerAccount(deviceId, customerAccount).ifPresent(deviceReg -> {
            throw new DuplicateAccountCreation("Duplicate Device Registration");
        });

        DeviceRegistration device = new DeviceRegistration();
        device.setDeviceId(deviceId);
        device.setCustomerAccount(customerAccount);
        device.setFcmToken(fcmToken);
        device.setPlatform(platform);
        customerAccount.addDevice(device);
        customerAccountRepository.save(customerAccount);
    }

    @Override
    public void refreshFcmToken(UUID customerId, String deviceId, String newFcmToken) {
        DeviceRegistration device = deviceRegRepository.findByDeviceIdAndCustomerAccountId(deviceId, customerId).orElseThrow(
                () -> new AccountNotFoundException("Device Not Found"));
        device.setFcmToken(newFcmToken);
        deviceRegRepository.save(device);
    }

    @Override
    public void removeFcmToken(UUID customerId, String deviceId) {
        DeviceRegistration device = deviceRegRepository.findByDeviceIdAndCustomerAccountId(deviceId, customerId)
                .orElseThrow(() -> new AccountNotFoundException("Device not found"));

        CustomerAccount account = device.getCustomerAccount();
        account.removeDevice(device);
        customerAccountRepository.save(account);
    }
}
