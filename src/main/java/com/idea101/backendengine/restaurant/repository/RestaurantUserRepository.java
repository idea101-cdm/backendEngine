package com.idea101.backendengine.restaurant.repository;

import com.idea101.backendengine.restaurant.entity.RestaurantUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RestaurantUserRepository extends JpaRepository<RestaurantUser, Long> {
    List<RestaurantUser> findByRestaurantId(Long restaurantId);
    List<RestaurantUser> findByUserId(UUID userId);
}
