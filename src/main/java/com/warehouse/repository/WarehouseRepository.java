package com.warehouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.entity.Warehouse;
import com.warehouse.enums.WarehouseStatus;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Optional<Warehouse> findByIdAndDeletedFalse(Long id);

    Page<Warehouse> findByDeletedFalse(Pageable pageable);

    List<Warehouse> findByStatusAndDeletedFalse(WarehouseStatus status);

    boolean existsByNameIgnoreCase(String name);
    
    Page<Warehouse> findAll(Pageable pageable);
}