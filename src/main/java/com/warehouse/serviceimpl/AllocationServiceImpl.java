package com.warehouse.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.warehouse.entity.Allocation;
import com.warehouse.entity.AllocationHistory;
import com.warehouse.entity.Product;
import com.warehouse.entity.WarehouseInventory;
import com.warehouse.enums.AllocationStatus;
import com.warehouse.exception.AllocationNotFoundException;
import com.warehouse.exception.InsufficientStockException;
import com.warehouse.exception.ProductNotFoundException;
import com.warehouse.mapper.AllocationMapper;
import com.warehouse.repository.AllocationHistoryRepository;
import com.warehouse.repository.AllocationRepository;
import com.warehouse.repository.ProductRepository;
import com.warehouse.repository.WarehouseInventoryRepository;
import com.warehouse.requestdto.AllocationRequest;
import com.warehouse.responsedto.AllocationResponse;
import com.warehouse.service.AllocationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(timeout = 30)
public class AllocationServiceImpl implements AllocationService {

    private final AllocationRepository allocationRepository;
    private final WarehouseInventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final AllocationHistoryRepository historyRepository;
    private final AllocationMapper allocationMapper;

    @Override
    public AllocationResponse allocateStock(AllocationRequest request) {

        log.info("Stock allocation started for Product Id : {}", request.getProductId());

        Product product = getProduct(request.getProductId());

        if (product.getTotalStock() < request.getQuantity()) {

            log.error("Allocation failed. Product {} has only {} units. Requested {}",product.getId(),product.getTotalStock(),request.getQuantity());
            throw new InsufficientStockException("Product total stock is insufficient.");
        }

        
        List<WarehouseInventory> inventories =inventoryRepository.findAvailableInventory(product.getId(),request.getQuantity());
        if (inventories.isEmpty()) {
            log.error("Allocation failed. No warehouse has sufficient stock for Product {}",product.getId());
            throw new InsufficientStockException("No warehouse has sufficient stock.");
        }
        
        WarehouseInventory inventory =selectBestWarehouse(inventories, request.getQuantity());
        if (inventory.getAvailableQuantity() < request.getQuantity()) {
            log.error("Allocation failed. Warehouse {} has only {} units.",inventory.getWarehouse().getId(),inventory.getAvailableQuantity());
            throw new InsufficientStockException("Insufficient stock available.");
        }
        
        log.info("Allocating {} units from warehouse {}",request.getQuantity(),inventory.getWarehouse().getName());

        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - request.getQuantity());
        inventoryRepository.save(inventory);

        product.setTotalStock(product.getTotalStock() - request.getQuantity());

        productRepository.save(product);

        log.info("Inventory updated successfully. Remaining quantity : {}",inventory.getAvailableQuantity());

        Allocation allocation = Allocation.builder()
                .product(product)
                .warehouse(inventory.getWarehouse())
                .quantity(request.getQuantity())
                .allocatedAt(LocalDateTime.now())
                .status(AllocationStatus.ALLOCATED)
                .build();

        allocation = allocationRepository.save(allocation);

        log.info("Allocation created successfully : {}", allocation.getId());

        AllocationHistory history = AllocationHistory.builder()
                .allocation(allocation)
                .status(AllocationStatus.ALLOCATED)
                .remarks("Allocated "
                        + request.getQuantity()
                        + " units from Warehouse "
                        + inventory.getWarehouse().getName())
                .createdAt(LocalDateTime.now())
                .build();

        historyRepository.save(history);

        log.info("Allocation history saved successfully.");

        return allocationMapper.toResponse(allocation);
    }

    
    private WarehouseInventory selectBestWarehouse(List<WarehouseInventory> inventories,
            Integer requiredQuantity) {

               return inventories.stream()
            		   .filter(i -> i.getAvailableQuantity() >= requiredQuantity)
            		   .max(java.util.Comparator.comparingInt(WarehouseInventory::getAvailableQuantity))
            		   .orElseThrow(() ->
            		   new InsufficientStockException("No warehouse has sufficient stock."));
}

    @Override
    @Transactional(readOnly = true)
    public AllocationResponse getAllocationById(Long id) {

        log.info("Fetching allocation : {}", id);

        Allocation allocation = getAllocation(id);

        return allocationMapper.toResponse(allocation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AllocationResponse> getAllAllocations(int page,int size,String sortBy,String direction) {
        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return allocationRepository
                .findAll(pageable)
                .map(allocationMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AllocationResponse> getAllocationByProduct(Long productId,int page,int size) {
        Pageable pageable = PageRequest.of(page, size);
        return allocationRepository
                .findByProductId(productId, pageable)
                .map(allocationMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AllocationResponse> getAllocationByWarehouse(Long warehouseId,int page,int size) {
        Pageable pageable = PageRequest.of(page, size);
        return allocationRepository
                .findByWarehouseId(warehouseId, pageable)
                .map(allocationMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AllocationResponse> getAllocationByDateRange(LocalDateTime from,LocalDateTime to,int page,int size) {
        Pageable pageable = PageRequest.of(page, size);
        return allocationRepository
                .findByAllocatedAtBetween(from, to, pageable)
                .map(allocationMapper::toResponse);
    }

    private Allocation getAllocation(Long id) {
        return allocationRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Allocation not found with id : {}", id);
                    return new AllocationNotFoundException("Allocation not found with id : " + id);
                });
    }

    private Product getProduct(Long productId) {
        return productRepository
                .findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> {

                    log.error("Product not found with id : {}", productId);

                    return new ProductNotFoundException(
                            "Product not found with id : " + productId);
                });
    }
}