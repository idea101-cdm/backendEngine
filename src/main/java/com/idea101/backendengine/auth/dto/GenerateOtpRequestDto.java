package com.idea101.backendengine.auth.dto;

import com.idea101.backendengine.common.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GenerateOtpRequestDto {

    private String phoneNumber;
    private String emailId;

    @NotNull(message = "Role is mandatory")
    private UserRole role;
}
