package com.idea101.backendengine.customer.service;

import com.idea101.backendengine.common.enums.Platform;

import java.util.UUID;

public interface DeviceRegService {
    void registerDevice(UUID customerId, String deviceId, Platform platform, String fcmToken);
    void refreshFcmToken(UUID customerId, String deviceId, String newFcmToken);
    void removeFcmToken(UUID customerId, String deviceId);
}
