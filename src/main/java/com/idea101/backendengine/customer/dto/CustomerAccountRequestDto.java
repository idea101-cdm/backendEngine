package com.idea101.backendengine.customer.dto;


import com.idea101.backendengine.common.enums.Sex;

import java.util.Date;

public class CustomerAccountRequestDto {
    private String name;
    private String email;
    private String phoneNumber;
    private Date dob;
    private Sex sex;
}
