package com.idea101.backendengine.customer.repository;

import com.idea101.backendengine.customer.entity.CustomerAccount;
import com.idea101.backendengine.customer.entity.DeviceRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeviceRegRepository extends JpaRepository<DeviceRegistration, UUID> {
    Optional<DeviceRegistration> findByDeviceIdAndCustomerAccount(String deviceId, CustomerAccount customerAccount);
    Optional<DeviceRegistration> findByDeviceIdAndCustomerAccountId(String deviceId, UUID accountId);
}
