package com.warehouse.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	Optional<Product> findByIdAndDeletedFalse(Long id);

	Optional<Product> findBySkuAndDeletedFalse(String sku);

	Optional<Product> findByNameIgnoreCaseAndDeletedFalse(String name);

	boolean existsBySkuAndDeletedFalse(String sku);

	boolean existsByNameIgnoreCaseAndDeletedFalse(String name);

	Page<Product> findByDeletedFalse(Pageable pageable);

}