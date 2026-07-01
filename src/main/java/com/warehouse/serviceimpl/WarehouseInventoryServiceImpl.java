package com.warehouse.serviceimpl;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.warehouse.entity.Product;
import com.warehouse.entity.Warehouse;
import com.warehouse.entity.WarehouseInventory;
import com.warehouse.enums.WarehouseStatus;
import com.warehouse.exception.DuplicateResourceException;
import com.warehouse.exception.InvalidRequestException;
import com.warehouse.exception.InventoryNotFoundException;
import com.warehouse.exception.ProductNotFoundException;
import com.warehouse.exception.WarehouseCapacityExceededException;
import com.warehouse.exception.WarehouseNotFoundException;
import com.warehouse.mapper.WarehouseInventoryMapper;
import com.warehouse.repository.ProductRepository;
import com.warehouse.repository.WarehouseInventoryRepository;
import com.warehouse.repository.WarehouseRepository;
import com.warehouse.requestdto.InventoryRequest;
import com.warehouse.responsedto.InventoryResponse;
import com.warehouse.service.WarehouseInventoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseInventoryServiceImpl implements WarehouseInventoryService {

    private final WarehouseInventoryRepository inventoryRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final WarehouseInventoryMapper inventoryMapper;
    
    @Override
    public InventoryResponse createInventory(InventoryRequest request) {
        log.info("Creating inventory");
        Warehouse warehouse = getWarehouse(request.getWarehouseId());
        Product product = getProduct(request.getProductId());

        validateWarehouseStatus(warehouse);
        validateQuantity(request.getAvailableQuantity());

        if (inventoryRepository.existsByWarehouseAndProduct(warehouse,product)) {
            throw new DuplicateResourceException("Inventory already exists for this warehouse and product.");
        }

        validateWarehouseCapacity(warehouse,request.getAvailableQuantity());
        WarehouseInventory inventory =inventoryMapper.toEntity(request,warehouse,product);
        inventory = inventoryRepository.save(inventory);

        log.info("Inventory created successfully with id : {}",inventory.getId());
        return inventoryMapper.toResponse(inventory);

    }
    
    @Override
    public InventoryResponse updateInventory(Long id,InventoryRequest request) {
        log.info("Updating inventory : {}", id);
        WarehouseInventory inventory =getInventory(id);
        validateQuantity(request.getAvailableQuantity());

        validateWarehouseStatus(inventory.getWarehouse());

        validateCapacityForUpdate(inventory,request.getAvailableQuantity());

        inventory.setAvailableQuantity(request.getAvailableQuantity());
        inventory = inventoryRepository.save(inventory);
        log.info("Inventory updated successfully : {}", id);
        return inventoryMapper.toResponse(inventory);

    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<InventoryResponse> getInventoryByWarehouse(Long warehouseId,int page,int size) {
        log.info("Fetching inventory by warehouse : {}",warehouseId);
        Pageable pageable =PageRequest.of(page, size);
        return inventoryRepository
                .findByWarehouseId(warehouseId,pageable)
                .map(inventoryMapper::toResponse);

    }
    
    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryById(Long id) {
        log.info("Fetching inventory : {}", id);
        return inventoryMapper.toResponse(getInventory(id));

    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<InventoryResponse> getAllInventory(int page,int size,String sortBy,String direction) {
        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =PageRequest.of(page, size, sort);
        return inventoryRepository
                .findAll(pageable)
                .map(inventoryMapper::toResponse);

    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<InventoryResponse> getInventoryByProduct(Long productId,int page,int size) {
        log.info("Fetching inventory by product : {}",productId);
        Pageable pageable =PageRequest.of(page, size);
        return inventoryRepository
                .findByProductId(productId,pageable)
                .map(inventoryMapper::toResponse);

    }
    
    @Override
    public void deleteInventory(Long id) {
        log.info("Deleting inventory : {}", id);
        WarehouseInventory inventory =getInventory(id);
        inventoryRepository.delete(inventory);
        log.info("Inventory deleted successfully : {}", id);

    }
    
    private void validateQuantity(Integer quantity) {
        if (quantity == null) {
            throw new InvalidRequestException("Quantity cannot be null.");

        }

        if (quantity < 0) {
            throw new InvalidRequestException("Quantity cannot be negative.");

        }

    }
    
    private void validateCapacityForUpdate(WarehouseInventory inventory,Integer newQuantity) {
        Integer totalQuantity = inventoryRepository.getTotalQuantityByWarehouse(inventory.getWarehouse().getId());
        totalQuantity = totalQuantity - inventory.getAvailableQuantity();
        if (totalQuantity + newQuantity> inventory.getWarehouse().getCapacity()) {
            throw new WarehouseCapacityExceededException("Warehouse capacity exceeded.");

        }
    }
    
    private Warehouse getWarehouse(Long id) {
        return warehouseRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new WarehouseNotFoundException("Warehouse not found with id : " + id));

    }
    
    private void validateWarehouseStatus(Warehouse warehouse) {
        log.info("========== DEBUG ==========");
        log.info("Warehouse Id: {}", warehouse.getId());
        log.info("Warehouse Status: {}", warehouse.getStatus());

        if (warehouse.getStatus() == WarehouseStatus.ACTIVE) {
            log.info("Warehouse is ACTIVE");
        } else {
            log.info("Warehouse is NOT ACTIVE");
        }

        if (!WarehouseStatus.ACTIVE.equals(warehouse.getStatus())) {
            throw new InvalidRequestException("Warehouse is inactive.");
        }
    }
    
    
    private Product getProduct(Long id) {
        return productRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id : " + id));

    }

    private WarehouseInventory getInventory(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new InventoryNotFoundException("Inventory not found with id : " + id));

    }

    
    private void validateWarehouseCapacity(Warehouse warehouse,Integer quantity) {
        Integer currentQuantity =inventoryRepository.getTotalQuantityByWarehouse(warehouse.getId());

        if (currentQuantity + quantity> warehouse.getCapacity()) {
            throw new WarehouseCapacityExceededException("Warehouse capacity exceeded.");

        }

    }
    }
