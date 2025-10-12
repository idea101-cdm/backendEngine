package com.idea101.backendengine.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateEmailRequest {

    @NotBlank(message = "New email must not be blank")
    @Email(message = "New email must be a valid email address")
    private String newEmail;
}
