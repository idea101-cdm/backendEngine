package com.idea101.backendengine.auth.service;

import com.idea101.backendengine.auth.entity.OtpCode;
import com.idea101.backendengine.auth.entity.User;
import com.idea101.backendengine.common.enums.OtpReason;

import java.util.UUID;

public interface OtpService {
    UUID requestOtp(User user, String phoneNumber, String email, OtpReason reason);
    OtpCode verifyOtp(UUID otpId, String code);
}
