package com.idea101.backendengine.auth.service;

import com.idea101.backendengine.common.enums.UserRole;

import java.util.UUID;

public interface AuthService {

    UUID initiateLoginViaOtp(String identifier, UserRole role); // phone or email
    String verifyLoginOtp(UUID otpId, String otpCode);

    UUID initiatePhoneUpdate(UUID userId, String newPhoneNumber);
    void verifyPhoneUpdateOtp(UUID otpId, String otpCode, String newPhoneNumber);

    UUID initiateEmailUpdate(UUID userId, String newEmail);
    void verifyEmailUpdateOtp(UUID otpId, String otpCode, String newEmail);
}
