package com.idea101.backendengine.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdatePhoneRequest {

    @NotBlank(message = "New phone number must not be blank")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be a valid 10-digit number")
    private String newPhoneNumber;
}
