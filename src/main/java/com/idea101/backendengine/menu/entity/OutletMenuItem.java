package com.idea101.backendengine.menu.entity;

import com.idea101.backendengine.restaurant.entity.Outlet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "outlet_menu_item",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_outlet_item", columnNames = {"outlet_id", "item_id"})
        })
@Getter
@Setter
public class OutletMenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outlet_id", nullable = false)
    private Outlet outlet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private MenuItem item;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Boolean isAvailable = true;

    @Column(nullable = false)
    private Boolean isActive = true;

    private Integer prepTimeOverride;

    private Integer stockQty;

    private BigDecimal taxPercent;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
