package com.idea101.backendengine.customer.dto;

import com.idea101.backendengine.common.enums.Sex;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class CustomerAccountResponseDto {
    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    private Date dob;
    private Sex sex;
    private LocalDateTime joinDate;
}
