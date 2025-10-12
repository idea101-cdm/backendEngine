package com.idea101.backendengine.customer.entity;

import com.idea101.backendengine.common.enums.CustomerAddressTag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer_addresses")
public class Address {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_account_id")
    private CustomerAccount customerAccount;

    private Double latitude;
    private Double longitude;

    private String manualAddress;

    private String receiverName;
    private String receiverPhone;

    @Enumerated(EnumType.STRING)
    private CustomerAddressTag addressTag;
    private String customAddressTag;

    private Boolean isDefault;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        if (addressTag == CustomerAddressTag.Other && (customAddressTag == null || customAddressTag.isBlank())) {
            customAddressTag = CustomerAddressTag.Other.toString();
        }

        updatedAt = LocalDateTime.now();
    }
}
