package com.idea101.backendengine.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class VerifyOtpRequestDto {
    @NotNull(message = "Id is mandatory")
    private UUID id;
    @NotNull(message = "Otp is mandatory")
    private String otpCode;
}
