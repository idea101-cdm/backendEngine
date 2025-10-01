package com.idea101.backendengine.restaurant.controller;

import com.idea101.backendengine.common.dto.RestaurantWithOutletsResponse;
import com.idea101.backendengine.restaurant.dto.AddUserRequest;
import com.idea101.backendengine.restaurant.dto.RestaurantCreateRequest;
import com.idea101.backendengine.restaurant.entity.Outlet;
import com.idea101.backendengine.restaurant.entity.Restaurant;
import com.idea101.backendengine.restaurant.entity.RestaurantUser;
import com.idea101.backendengine.restaurant.external.RestaurantExternalService;
import com.idea101.backendengine.restaurant.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantExternalService restaurantExternalService;
    public RestaurantController(RestaurantService restaurantService, RestaurantExternalService restaurantExternalService) {
        this.restaurantService = restaurantService;
        this.restaurantExternalService = restaurantExternalService;
    }

    @PostMapping
    public ResponseEntity<?> createRestaurant(@RequestBody RestaurantCreateRequest request,
                                              @RequestParam UUID ownerId) {


        Restaurant saved = restaurantService.createRestaurant(request, ownerId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getRestaurantId())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable Long id) {
        return restaurantService.getRestaurantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Restaurant>> getActiveRestaurants() {
        return ResponseEntity.ok(restaurantService.getActiveRestaurants());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(
            @PathVariable Long id,
            @RequestBody RestaurantCreateRequest request
    ) {
        Restaurant saved = restaurantService.updateRestaurant(id, request);
        return ResponseEntity.ok(saved);
    }


    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateRestaurant(@PathVariable Long id) {
        restaurantService.deactivateRestaurant(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateRestaurant(@PathVariable Long id) {
        restaurantService.activateRestaurant(id);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{id}/users")
    public ResponseEntity<RestaurantUser> addUserToRestaurant(
            @PathVariable Long id,
            @RequestBody AddUserRequest request
    ) {
        RestaurantUser added = restaurantService.addUserToRestaurant(id, request.getUserId(), request.getRole());
        return ResponseEntity.ok(added);
    }

    @DeleteMapping("/{id}/users/{userId}")
    public ResponseEntity<Void> removeUserFromRestaurant(
            @PathVariable Long id,
            @PathVariable UUID userId
    ) {
        restaurantService.removeUserFromRestaurant(id, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<RestaurantUser>> getUsersByRestaurant(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getUsersByRestaurant(id));
    }
    @GetMapping("/external/all")
    public ResponseEntity<List<RestaurantWithOutletsResponse>> getAllRestaurantsWithOutlets() {
        List<RestaurantWithOutletsResponse> list = restaurantExternalService.getAllRestaurantsWithOutlets();
        return ResponseEntity.ok(list);
    }
}
