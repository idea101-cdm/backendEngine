package com.idea101.backendengine.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OtpVerificationRequest {
    @NotNull(message = "OTP ID must not be null")
    private UUID otpId;

    @NotBlank(message = "OTP code must not be blank")
    private String otpCode;

}
