package com.idea101.backendengine.customer.entity;

import com.idea101.backendengine.common.enums.Sex;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer_accounts")
public class CustomerAccount {

    @Id
    private UUID id;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @OneToMany(mappedBy = "customerAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeviceRegistration> devices = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "customerAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    public void addDevice(DeviceRegistration device) {
        devices.add(device);
        device.setCustomerAccount(this);
    }

    public void removeDevice(DeviceRegistration device) {
        devices.remove(device);
        device.setCustomerAccount(null);
    }
}
