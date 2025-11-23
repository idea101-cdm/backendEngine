package com.idea101.backendengine.customer.dto;


import com.idea101.backendengine.common.enums.Platform;
import com.idea101.backendengine.common.enums.Sex;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CreateAccountDto {
    @NotBlank(message = "id is required")
    private UUID id;
    @NotBlank(message = "name is required")
    private String name;

    @Email(message = "should be a valid email")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "phone number must be 10 digits")
    private String phoneNumber;

    private LocalDate dob;

    private Sex sex;

    private String fcmToken;

    private Platform platform;

    private String deviceId;
}
