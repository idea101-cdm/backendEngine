package com.idea101.backendengine.menu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item_category_map",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_item_category", columnNames = {"item_id", "category_id"})
        })
@Getter
@Setter
public class ItemCategoryMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private MenuItem item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private MenuCategory category;

    @Column(nullable = false)
    private Integer position = 0;
}
