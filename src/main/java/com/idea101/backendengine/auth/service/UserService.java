package com.idea101.backendengine.auth.service;

import com.idea101.backendengine.auth.dto.VerifyOtpRequestDto;

import java.util.UUID;

public interface UserService {
    String loginUserWithOtp(VerifyOtpRequestDto verifyOtpRequestDto);
    void updateCredentials(UUID id, String email, String phoneNumber);
    void activateUser(UUID id);
    void deactivateUser(UUID id);
    void deleteUser(UUID id);
}
