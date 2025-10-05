package com.idea101.backendengine.menu.entity;

import com.idea101.backendengine.menu.enums.VegType;
import com.idea101.backendengine.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "menu_item")
@Getter
@Setter
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(length = 128)
    private String sku;  // identifier for restaurant

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;


    @Enumerated(EnumType.STRING)
    @Column(name = "veg_type", length = 32)
    private VegType vegType = VegType.UNKNOWN;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false)
    private Boolean isVisible = true;

    private Integer defaultPrepTimeMins;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MenuItemImage> images;
}
