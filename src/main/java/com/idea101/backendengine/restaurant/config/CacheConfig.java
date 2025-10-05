package com.idea101.backendengine.restaurant.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Arrays;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String RESTAURANTS_WITH_OUTLETS = "restaurants_with_outlets";
    public static final String RESTAURANT_BY_ID = "restaurant_by_id";
    public static final String OUTLET_BY_ID = "outlet_by_id";
    public static final String OUTLET_HOURS_BY_OUTLET = "outlet_hours_by_outlet";
    public static final String SEARCH_RESTAURANTS = "search_restaurants";

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        CaffeineCache restaurantsWithOutlets = new CaffeineCache(
                RESTAURANTS_WITH_OUTLETS,
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofSeconds(120))
                        .maximumSize(500)
                        .build()
        );

        CaffeineCache restaurantById = new CaffeineCache(
                RESTAURANT_BY_ID,
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(10))
                        .maximumSize(10000)
                        .build()
        );

        CaffeineCache outletById = new CaffeineCache(
                OUTLET_BY_ID,
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(10))
                        .maximumSize(20000)
                        .build()
        );

        CaffeineCache outletHours = new CaffeineCache(
                OUTLET_HOURS_BY_OUTLET,
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(5))
                        .maximumSize(20000)
                        .build()
        );

        CaffeineCache searchCache = new CaffeineCache(
                SEARCH_RESTAURANTS,
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofSeconds(60))
                        .maximumSize(1000)
                        .build()
        );

        cacheManager.setCaches(Arrays.asList(
                restaurantsWithOutlets, restaurantById, outletById, outletHours, searchCache
        ));
        return cacheManager;
    }
}
