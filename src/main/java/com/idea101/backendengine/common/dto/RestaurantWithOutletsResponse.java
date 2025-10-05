package com.idea101.backendengine.common.dto;

import lombok.Data;
import java.util.List;

@Data
public class RestaurantWithOutletsResponse {

    private Long restaurantId;
    private String name;
    private String description;
    private String cuisineTypes;
    private String logoUrl;

    private List<OutletInfo> outlets;

    @Data
    public static class OutletInfo {
        private Long outletId;
        private String name;
        private String address;
        private String city;
        private String state;
        private String zipCode;
        private String contactPhone;
        private String contactEmail;
        private Boolean isActive;
        private Boolean isVerified;
        private Double latitude;
        private Double longitude;
    }
}
