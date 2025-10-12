package com.idea101.backendengine.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.util.UUID;

@Getter
public class VerifyPhoneOtpRequest {

    @NotNull(message = "OTP ID must not be null")
    private UUID otpId;

    @NotBlank(message = "OTP code must not be blank")
    private String otpCode;

    @NotBlank(message = "New phone number must not be blank")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be a valid 10-digit number")
    private String newPhoneNumber;
}
