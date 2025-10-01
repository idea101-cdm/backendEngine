package com.idea101.backendengine.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "outlet")
@Getter
@Setter
public class Outlet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outletId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 1024)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 30)
    private String zipCode;

    @Column(length = 20)
    private String contactPhone;

    @Column(length = 150)
    private String contactEmail;

    @Column(length = 100)
    private String gstNumber;

    @Column(length = 100)
    private String licenseNumber;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false)
    private Boolean isVerified = false;

    private Double latitude;
    private Double longitude;

    @Column(name = "is_default_outlet", nullable = false)
    private Boolean isDefaultOutlet = false;

    @ElementCollection
    @CollectionTable(name = "outlet_images", joinColumns = @JoinColumn(name = "outlet_id"))
    @Column(name = "image_url", length = 1024)
    private List<String> images;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
