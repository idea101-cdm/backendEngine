package com.idea101.backendengine.restaurant.dto;

import com.idea101.backendengine.restaurant.entity.RestaurantUser;
import lombok.Data;

import java.util.UUID;

@Data
public class AddUserRequest {
    private UUID userId;
    private RestaurantUser.Role role;
}
