package com.idea101.backendengine.restaurant.repository;

import com.idea101.backendengine.restaurant.entity.Outlet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutletRepository extends JpaRepository<Outlet, Long> {

    List<Outlet> findByRestaurant_RestaurantId(Long restaurantId);

    List<Outlet> findByRestaurant_RestaurantIdAndIsActiveTrue(Long restaurantId);

    List<Outlet> findByCityIgnoreCase(String city);
}
