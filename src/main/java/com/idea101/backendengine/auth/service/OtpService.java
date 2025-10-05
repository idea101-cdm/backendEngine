package com.idea101.backendengine.auth.service;

import com.idea101.backendengine.auth.dto.GenerateOtpRequestDto;
import com.idea101.backendengine.auth.dto.VerifyOtpRequestDto;

import java.util.UUID;

public interface OtpService {
    UUID requestOtp(GenerateOtpRequestDto requestOtpDto);
    String verifyOtp(VerifyOtpRequestDto verifyOtpDto);
}
