package com.idea101.backendengine.restaurant.service;

import com.idea101.backendengine.restaurant.config.CacheConfig;
import com.idea101.backendengine.restaurant.dto.RestaurantCreateRequest;
import com.idea101.backendengine.restaurant.entity.Outlet;
import com.idea101.backendengine.restaurant.entity.Restaurant;
import com.idea101.backendengine.restaurant.entity.RestaurantUser;
import com.idea101.backendengine.restaurant.repository.OutletRepository;
import com.idea101.backendengine.restaurant.repository.RestaurantRepository;
import com.idea101.backendengine.restaurant.repository.RestaurantUserRepository;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantUserRepository restaurantUserRepository;
    private final OutletRepository outletRepository;
    private final CacheManager cacheManager;

    public RestaurantService(RestaurantRepository restaurantRepository,
                             RestaurantUserRepository restaurantUserRepository,
                             OutletRepository outletRepository,
                             CacheManager cacheManager) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantUserRepository = restaurantUserRepository;
        this.outletRepository = outletRepository;
        this.cacheManager = cacheManager;
    }


    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConfig.RESTAURANTS_WITH_OUTLETS, key = "'all'"),
            @CacheEvict(cacheNames = CacheConfig.SEARCH_RESTAURANTS, allEntries = true)
    })
    public Restaurant createRestaurant(RestaurantCreateRequest request, UUID ownerId) {

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name is required");
        }
        if (request.getOutletName() == null || request.getOutletName().trim().isEmpty()) {
            throw new IllegalArgumentException("Outlet name is required");
        }
        if (request.getOutletAddress() == null || request.getOutletAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Outlet address is required");
        }

        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName().trim());
        restaurant.setDescription(request.getDescription());
        restaurant.setCuisineTypes(request.getCuisineTypes());
        restaurant.setContactEmail(request.getContactEmail());
        restaurant.setContactPhone(request.getContactPhone());
        restaurant.setLogoUrl(request.getLogoUrl());
        restaurant.setIsActive(true);
        restaurant = restaurantRepository.save(restaurant);

        RestaurantUser ownerLink = new RestaurantUser();
        ownerLink.setRestaurantId(restaurant.getRestaurantId());
        ownerLink.setUserId(ownerId);
        ownerLink.setRole(RestaurantUser.Role.OWNER);
        ownerLink.setIsActive(true);
        restaurantUserRepository.save(ownerLink);

        Outlet outlet = new Outlet();
        outlet.setRestaurant(restaurant);
        outlet.setName(request.getOutletName());
        outlet.setAddress(request.getOutletAddress());
        outlet.setCity(request.getOutletCity());
        outlet.setState(request.getOutletState());
        outlet.setZipCode(request.getOutletZipCode());
        outlet.setContactPhone(request.getOutletPhone());
        outlet.setContactEmail(request.getOutletEmail());
        outlet.setGstNumber(request.getOutletGstNumber());
        outlet.setLicenseNumber(request.getOutletLicenseNumber());
        outlet.setIsActive(true);
        outlet.setIsVerified(false);
        outlet.setIsDefaultOutlet(true);
        Outlet savedOutlet = outletRepository.save(outlet);

        if (cacheManager.getCache(CacheConfig.OUTLET_BY_ID) != null) {
            cacheManager.getCache(CacheConfig.OUTLET_BY_ID).evict(savedOutlet.getOutletId());
        }

        return restaurant;
    }

    @Cacheable(cacheNames = CacheConfig.RESTAURANTS_WITH_OUTLETS, key = "'all'")
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Cacheable(cacheNames = CacheConfig.RESTAURANT_BY_ID, key = "#restaurantId")
    public Optional<Restaurant> getRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId);
    }

    @Cacheable(cacheNames = CacheConfig.RESTAURANTS_WITH_OUTLETS, key = "'active'")
    public List<Restaurant> getActiveRestaurants() {
        return restaurantRepository.findByIsActiveTrue();
    }

    @Cacheable(cacheNames = CacheConfig.SEARCH_RESTAURANTS, key = "#name")
    public List<Restaurant> findByName(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name);
    }

    @Cacheable(cacheNames = CacheConfig.SEARCH_RESTAURANTS, key = "#cuisine")
    public List<Restaurant> findByCuisine(String cuisine) {
        return restaurantRepository.findByCuisineTypesContainingIgnoreCase(cuisine);
    }

    @Cacheable(cacheNames = CacheConfig.RESTAURANTS_WITH_OUTLETS, key = "'verified_active'")
    public List<Restaurant> getVerifiedAndActiveRestaurants() {
        return restaurantRepository.findByIsActiveTrueAndIsVerifiedTrue();
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConfig.RESTAURANT_BY_ID, key = "#restaurantId"),
            @CacheEvict(cacheNames = CacheConfig.RESTAURANTS_WITH_OUTLETS, key = "'all'"),
            @CacheEvict(cacheNames = CacheConfig.SEARCH_RESTAURANTS, allEntries = true)
    })
    public Restaurant updateRestaurant(Long restaurantId, RestaurantCreateRequest request) {
        Restaurant existing = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setCuisineTypes(request.getCuisineTypes());
        existing.setContactEmail(request.getContactEmail());
        existing.setContactPhone(request.getContactPhone());
        existing.setLogoUrl(request.getLogoUrl());

        Restaurant saved = restaurantRepository.save(existing);

        if (cacheManager.getCache(CacheConfig.RESTAURANT_BY_ID) != null) {
            cacheManager.getCache(CacheConfig.RESTAURANT_BY_ID).evict(restaurantId);
        }

        return saved;
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConfig.RESTAURANT_BY_ID, key = "#restaurantId"),
            @CacheEvict(cacheNames = CacheConfig.RESTAURANTS_WITH_OUTLETS, key = "'all'"),
            @CacheEvict(cacheNames = CacheConfig.SEARCH_RESTAURANTS, allEntries = true)
    })
    public void deactivateRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        restaurant.setIsActive(false);
        restaurantRepository.save(restaurant);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConfig.RESTAURANT_BY_ID, key = "#restaurantId"),
            @CacheEvict(cacheNames = CacheConfig.RESTAURANTS_WITH_OUTLETS, key = "'all'"),
            @CacheEvict(cacheNames = CacheConfig.SEARCH_RESTAURANTS, allEntries = true)
    })
    public void activateRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        restaurant.setIsActive(true);
        restaurantRepository.save(restaurant);
    }



    @Transactional
    public RestaurantUser addUserToRestaurant(Long restaurantId, UUID userId, RestaurantUser.Role role) {
        List<RestaurantUser> existing = restaurantUserRepository.findByRestaurantId(restaurantId);
        boolean alreadyLinked = existing.stream()
                .anyMatch(ru -> ru.getUserId().equals(userId));
        if (alreadyLinked) {
            throw new RuntimeException("User already linked to restaurant");
        }

        RestaurantUser restaurantUser = new RestaurantUser();
        restaurantUser.setRestaurantId(restaurantId);
        restaurantUser.setUserId(userId);
        restaurantUser.setRole(role);
        restaurantUser.setIsActive(true);

        return restaurantUserRepository.save(restaurantUser);
    }

    @Transactional
    public void removeUserFromRestaurant(Long restaurantId, UUID userId) {
        List<RestaurantUser> existing = restaurantUserRepository.findByRestaurantId(restaurantId);
        existing.stream()
                .filter(ru -> ru.getUserId().equals(userId))
                .forEach(ru -> ru.setIsActive(false));
        restaurantUserRepository.saveAll(existing);
        if (cacheManager.getCache(CacheConfig.RESTAURANTS_WITH_OUTLETS) != null) {
            cacheManager.getCache(CacheConfig.RESTAURANTS_WITH_OUTLETS).evict("all");
        }
    }

    public List<RestaurantUser> getUsersByRestaurant(Long restaurantId) {
        return restaurantUserRepository.findByRestaurantId(restaurantId);
    }

    public List<RestaurantUser> getRestaurantsByUser(UUID userId) {
        return restaurantUserRepository.findByUserId(userId);
    }
}
