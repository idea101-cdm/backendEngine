package com.idea101.backendengine.auth.dto;

import com.idea101.backendengine.common.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class OtpLoginRequest {

    @Pattern(
            regexp = "^(\\d{10}|[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$",
            message = "Identifier must be a 10-digit number or a valid email address"
    )
    @NotBlank(message = "Identifier is required")
    private String identifier;
    @NotNull(message = "Role must be provided")
    private UserRole role;
}
