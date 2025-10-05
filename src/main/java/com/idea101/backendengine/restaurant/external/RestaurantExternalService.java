package com.idea101.backendengine.restaurant.external;

import com.idea101.backendengine.common.dto.RestaurantWithOutletsResponse;
import java.util.List;

public interface RestaurantExternalService {
    List<RestaurantWithOutletsResponse> getAllRestaurantsWithOutlets();
}