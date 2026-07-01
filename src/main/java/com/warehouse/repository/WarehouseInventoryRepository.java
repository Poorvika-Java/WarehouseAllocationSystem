package com.warehouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.warehouse.entity.Product;
import com.warehouse.entity.Warehouse;
import com.warehouse.entity.WarehouseInventory;

import jakarta.persistence.LockModeType;

public interface WarehouseInventoryRepository extends JpaRepository<WarehouseInventory, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<WarehouseInventory> findByWarehouseAndProduct(
            Warehouse warehouse,
            Product product
    );

    boolean existsByWarehouseAndProduct(
            Warehouse warehouse,
            Product product
    );

    Page<WarehouseInventory> findAll(Pageable pageable);

    Page<WarehouseInventory> findByWarehouseId(Long warehouseId, Pageable pageable);

    Page<WarehouseInventory> findByProductId(Long productId, Pageable pageable);

    @Query("""
        SELECT COALESCE(SUM(i.availableQuantity), 0)
        FROM WarehouseInventory i
        WHERE i.warehouse.id = :warehouseId
    """)
    Integer getTotalQuantityByWarehouse(@Param("warehouseId") Long warehouseId);

    @Query("""
        SELECT wi
        FROM WarehouseInventory wi
        WHERE wi.product.id = :productId
        AND wi.availableQuantity >= :quantity
        ORDER BY wi.availableQuantity DESC
    """)
    List<WarehouseInventory> findAvailableInventory(
            @Param("productId") Long productId,
            @Param("quantity") Integer quantity
    );
}