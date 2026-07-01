package com.warehouse.entity;

import java.time.LocalDateTime;
import com.warehouse.enums.AllocationStatus;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "allocation")
@Data
public class Allocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    private Integer quantity;

    private LocalDateTime allocatedAt;

    @Enumerated(EnumType.STRING)
    private AllocationStatus status;

}