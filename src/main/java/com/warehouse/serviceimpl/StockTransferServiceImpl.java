package com.warehouse.serviceimpl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.warehouse.entity.Product;
import com.warehouse.entity.StockTransfer;
import com.warehouse.entity.Warehouse;
import com.warehouse.entity.WarehouseInventory;
import com.warehouse.enums.TransferStatus;
import com.warehouse.exception.InsufficientStockException;
import com.warehouse.exception.ProductNotFoundException;
import com.warehouse.exception.StockTransferNotFoundException;
import com.warehouse.exception.WarehouseCapacityExceededException;
import com.warehouse.exception.WarehouseNotFoundException;
import com.warehouse.mapper.StockTransferMapper;
import com.warehouse.repository.ProductRepository;
import com.warehouse.repository.StockTransferRepository;
import com.warehouse.repository.WarehouseInventoryRepository;
import com.warehouse.repository.WarehouseRepository;
import com.warehouse.requestdto.StockTransferRequest;
import com.warehouse.responsedto.StockTransferResponse;
import com.warehouse.service.StockTransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(timeout = 30)
public class StockTransferServiceImpl implements StockTransferService {

    private final StockTransferRepository transferRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseInventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final StockTransferMapper transferMapper;

    @Override
    public StockTransferResponse transferStock(StockTransferRequest request) {

        log.info("Stock transfer started.");

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            log.error("Transfer failed. Invalid quantity : {}", request.getQuantity());
            throw new IllegalArgumentException("Transfer quantity must be greater than zero.");
        }
        
        if (request.getSourceWarehouseId().equals(request.getTargetWarehouseId())) {
            log.error("Transfer failed. Source and Target warehouse are same : {}",request.getSourceWarehouseId());
            throw new IllegalArgumentException("Source and Target warehouse cannot be same.");
        }

        Warehouse source = getWarehouse(request.getSourceWarehouseId());
        Warehouse target = getWarehouse(request.getTargetWarehouseId());
        Product product = getProduct(request.getProductId());

        WarehouseInventory sourceInventory = inventoryRepository
                .findByWarehouseAndProduct(source, product)
                .orElseThrow(() -> new InsufficientStockException(
                        "Product not found in source warehouse."));

        if (sourceInventory.getAvailableQuantity() < request.getQuantity()) {
            log.error("Transfer failed. Source warehouse {} has only {} units.",source.getId(),sourceInventory.getAvailableQuantity());
            throw new InsufficientStockException("Insufficient stock in source warehouse.");
        }
        
        WarehouseInventory targetInventory = inventoryRepository
                .findByWarehouseAndProduct(target, product)
                .orElseGet(() -> {
                    WarehouseInventory inventory = new WarehouseInventory();
                    inventory.setWarehouse(target);
                    inventory.setProduct(product);
                    inventory.setAvailableQuantity(0);
                    return inventory;
                });

        Integer currentTotal = inventoryRepository
                .getTotalQuantityByWarehouse(target.getId());

        if (currentTotal == null) {
            currentTotal = 0;
        }

        if (target.getCapacity() == null) {
            log.error("Transfer failed. Capacity not configured for warehouse {}",target.getId());
            throw new WarehouseCapacityExceededException("Target warehouse capacity is not configured.");
        }

        if (currentTotal + request.getQuantity() > target.getCapacity()) {
            log.error("Transfer failed. Warehouse {} capacity exceeded. Current={}, Requested={}, Capacity={}",target.getId(),
                    currentTotal,request.getQuantity(),target.getCapacity());
            throw new WarehouseCapacityExceededException("Target warehouse capacity exceeded.");
        }

        sourceInventory.setAvailableQuantity(
                sourceInventory.getAvailableQuantity() - request.getQuantity());

        targetInventory.setAvailableQuantity(
                targetInventory.getAvailableQuantity() + request.getQuantity());

        inventoryRepository.save(sourceInventory);
        inventoryRepository.save(targetInventory);

        log.info("Source Warehouse Remaining Stock : {}",
                sourceInventory.getAvailableQuantity());

        log.info("Target Warehouse Available Stock : {}",
                targetInventory.getAvailableQuantity());

        StockTransfer transfer = StockTransfer.builder()
                .sourceWarehouse(source)
                .targetWarehouse(target)
                .product(product)
                .quantity(request.getQuantity())
                .transferDate(LocalDateTime.now())
                .status(TransferStatus.COMPLETED)
                .build();

        transfer = transferRepository.save(transfer);

        log.info("Transfer Id : {}", transfer.getId());
        log.info("Transferred {} units of Product {} from {} to {}",
                request.getQuantity(),
                product.getName(),
                source.getName(),
                target.getName());

        log.info("Stock transfer completed successfully.");
        return transferMapper.toResponse(transfer);
    }

    @Override
    @Transactional(readOnly = true)
    public StockTransferResponse getTransferById(Long id) {

        log.info("Fetching transfer : {}", id);

        return transferMapper.toResponse(getTransfer(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockTransferResponse> getAllTransfers(int page,int size,String sortBy,String direction) {
        log.info("Fetching all stock transfers.");
        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return transferRepository.findAll(pageable)
                .map(transferMapper::toResponse);
    }

    private Warehouse getWarehouse(Long id) {
        return warehouseRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {
                    log.error("Warehouse not found with id : {}", id);
                    return new WarehouseNotFoundException("Warehouse not found with id : " + id);
                });
    }

    private Product getProduct(Long id) {
        return productRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {
                    log.error("Product not found with id : {}", id);
                    return new ProductNotFoundException("Product not found with id : " + id);
                });
    }

    private StockTransfer getTransfer(Long id) {
        return transferRepository
                .findById(id)
                .orElseThrow(() -> {

                    log.error("Transfer not found with id : {}", id);
                    return new StockTransferNotFoundException("Transfer not found with id : " + id);
                });
    }
}