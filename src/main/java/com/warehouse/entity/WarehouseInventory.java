package com.warehouse.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "warehouse_inventory",uniqueConstraints = { @UniqueConstraint(name = "uk_inventory_warehouse_product",
                        columnNames = {"warehouse_id","product_id"
                        }
                )
        },
        indexes = {
                @Index(name = "idx_inventory_product",columnList = "product_id"),
                @Index(name = "idx_inventory_warehouse",columnList = "warehouse_id")
        }
)

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseInventory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer availableQuantity;

    
     //Optimistic Locking    
    @Version
    private Long version;

}