package com.idea101.backendengine.restaurant.external;

import com.idea101.backendengine.common.dto.RestaurantWithOutletsResponse;
import com.idea101.backendengine.common.dto.RestaurantWithOutletsResponse.OutletInfo;
import com.idea101.backendengine.restaurant.entity.Outlet;
import com.idea101.backendengine.restaurant.entity.Restaurant;
import com.idea101.backendengine.restaurant.service.RestaurantService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RestaurantExternalServiceImpl implements RestaurantExternalService {

    private final RestaurantService restaurantService;

    public RestaurantExternalServiceImpl(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Override
    public List<RestaurantWithOutletsResponse> getAllRestaurantsWithOutlets() {

        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        if (restaurants == null || restaurants.isEmpty()) return List.of();

        List<RestaurantWithOutletsResponse> result = new ArrayList<>(restaurants.size());
        for (Restaurant r : restaurants) {
            RestaurantWithOutletsResponse resp = mapRestaurantToDto(r);
            result.add(resp);
        }
        return result;
    }

    private RestaurantWithOutletsResponse mapRestaurantToDto(Restaurant r) {
        RestaurantWithOutletsResponse dto = new RestaurantWithOutletsResponse();
        dto.setRestaurantId(r.getRestaurantId());
        dto.setName(r.getName());
        dto.setDescription(r.getDescription());
        dto.setCuisineTypes(r.getCuisineTypes());
        dto.setLogoUrl(r.getLogoUrl());

        List<OutletInfo> outlets = (r.getOutlets() == null) ?
                List.of() :
                r.getOutlets().stream()
                        .filter(Outlet::getIsActive)
                        .map(this::mapOutletToDto)
                        .collect(Collectors.toList());
        dto.setOutlets(outlets);
        return dto;

    }


    private OutletInfo mapOutletToDto(Outlet o) {
        OutletInfo oi = new OutletInfo();
        oi.setOutletId(o.getOutletId());
        oi.setName(o.getName());
        oi.setAddress(o.getAddress());
        oi.setCity(o.getCity());
        oi.setState(o.getState());
        oi.setZipCode(o.getZipCode());
        oi.setContactPhone(o.getContactPhone());
        oi.setContactEmail(o.getContactEmail());
        oi.setIsActive(o.getIsActive());
        oi.setIsVerified(o.getIsVerified());
        oi.setLatitude(o.getLatitude());
        oi.setLongitude(o.getLongitude());
        return oi;
    }
}
