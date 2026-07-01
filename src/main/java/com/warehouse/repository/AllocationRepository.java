package com.warehouse.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.warehouse.entity.Allocation;

public interface AllocationRepository extends JpaRepository<Allocation, Long> {

    Page<Allocation> findByProductId(Long productId,Pageable pageable);

    Page<Allocation> findByWarehouseId(Long warehouseId,Pageable pageable);

    Page<Allocation> findByAllocatedAtBetween(LocalDateTime from,LocalDateTime to,Pageable pageable);

}