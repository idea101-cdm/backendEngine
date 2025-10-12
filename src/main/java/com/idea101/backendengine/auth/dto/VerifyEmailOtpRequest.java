package com.idea101.backendengine.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class VerifyEmailOtpRequest {

    @NotNull(message = "OTP ID must not be null")
    private UUID otpId;

    @NotBlank(message = "OTP code must not be blank")
    private String otpCode;

    @NotBlank(message = "New email must not be blank")
    @Email(message = "New email must be a valid email address")
    private String newEmail;
}
