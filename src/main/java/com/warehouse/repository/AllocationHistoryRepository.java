package com.warehouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.entity.AllocationHistory;

public interface AllocationHistoryRepository extends JpaRepository<AllocationHistory, Long> {

    Page<AllocationHistory> findByAllocationProductId(Long productId,Pageable pageable);

    Page<AllocationHistory> findByAllocationWarehouseId(Long warehouseId,Pageable pageable);

}