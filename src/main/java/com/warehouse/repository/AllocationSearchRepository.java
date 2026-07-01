package com.warehouse.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.warehouse.entity.Allocation;

public interface AllocationSearchRepository extends JpaRepository<Allocation, Long> {

    @Query("""
        SELECT a FROM Allocation a
        WHERE
        (:warehouseId IS NULL OR a.warehouse.id=:warehouseId)
        AND
        (:productId IS NULL OR a.product.id=:productId)
        AND
        (:start IS NULL OR a.allocatedAt>=:start)
        AND
        (:end IS NULL OR a.allocatedAt<=:end)
        """)
    Page<Allocation> searchAllocations (Long warehouseId,Long productId,LocalDateTime start,LocalDateTime end,Pageable pageable);

}