package com.idea101.backendengine.restaurant.dto;

import lombok.Data;

@Data
public class RestaurantCreateRequest {
    private String name;
    private String description;
    private String cuisineTypes;
    private String contactEmail;
    private String contactPhone;
    private String logoUrl;

    private String outletName;
    private String outletAddress;
    private String outletCity;
    private String outletState;
    private String outletZipCode;
    private String outletPhone;
    private String outletEmail;
    private String outletGstNumber;
    private String outletLicenseNumber;
}
