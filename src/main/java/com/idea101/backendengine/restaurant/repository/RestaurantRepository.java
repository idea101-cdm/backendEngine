package com.idea101.backendengine.restaurant.repository;

import com.idea101.backendengine.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByIsActiveTrue();

    List<Restaurant> findByIsVerified(Boolean isVerified);

    List<Restaurant> findByNameContainingIgnoreCase(String name);

    List<Restaurant> findByCuisineTypesContainingIgnoreCase(String cuisineType);

    List<Restaurant> findByIsActiveTrueAndIsVerifiedTrue();
}
