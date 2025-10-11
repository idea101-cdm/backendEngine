package com.idea101.backendengine.auth.service;

import com.idea101.backendengine.auth.dto.GenerateOtpRequestDto;
import com.idea101.backendengine.auth.dto.VerifyOtpRequestDto;
import com.idea101.backendengine.auth.entity.OtpCode;

import java.util.UUID;

public interface OtpService {
    UUID requestOtp(GenerateOtpRequestDto requestOtpDto);
    OtpCode verifyOtp(VerifyOtpRequestDto verifyOtpDto);
}
