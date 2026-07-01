package com.warehouse.ServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.warehouse.entity.Product;
import com.warehouse.entity.Warehouse;
import com.warehouse.entity.WarehouseInventory;
import com.warehouse.enums.WarehouseStatus;
import com.warehouse.exception.*;
import com.warehouse.mapper.WarehouseInventoryMapper;
import com.warehouse.repository.ProductRepository;
import com.warehouse.repository.WarehouseInventoryRepository;
import com.warehouse.repository.WarehouseRepository;
import com.warehouse.requestdto.InventoryRequest;
import com.warehouse.responsedto.InventoryResponse;
import com.warehouse.serviceimpl.WarehouseInventoryServiceImpl;

@ExtendWith(MockitoExtension.class)
class WarehouseInventoryServiceImplTest {

    @Mock
    private WarehouseInventoryRepository inventoryRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WarehouseInventoryMapper inventoryMapper;

    @InjectMocks
    private WarehouseInventoryServiceImpl inventoryService;

    private Warehouse warehouse;
    private Product product;
    private WarehouseInventory inventory;
    private InventoryRequest request;
    private InventoryResponse response;

    @BeforeEach
    void setUp() {

    	warehouse = Warehouse.builder()
    	        .id(1L)
    	        .name("Warehouse A")
    	        .capacity(1000)
    	        .status(WarehouseStatus.ACTIVE)
    	        .build();

    	warehouse.setDeleted(false);

        product = Product.builder()
                .id(1L)
                .name("Laptop")
                .sku("LAP100")
                .deleted(false)
                .build();

        inventory = WarehouseInventory.builder()
                .id(1L)
                .warehouse(warehouse)
                .product(product)
                .availableQuantity(100)
                .build();

        request = new InventoryRequest();
        request.setWarehouseId(1L);
        request.setProductId(1L);
        request.setAvailableQuantity(100);

        response = InventoryResponse.builder()
                .id(1L)
                .warehouseId(1L)
                .warehouseName("Warehouse A")
                .productId(1L)
                .productName("Laptop")
                .sku("LAP100")
                .availableQuantity(100)
                .build();
    }


@Test
void testCreateInventory_Success() {

    when(warehouseRepository.findByIdAndDeletedFalse(1L))
            .thenReturn(Optional.of(warehouse));

    when(productRepository.findByIdAndDeletedFalse(1L))
            .thenReturn(Optional.of(product));

    when(inventoryRepository.existsByWarehouseAndProduct(warehouse, product))
            .thenReturn(false);

    when(inventoryRepository.getTotalQuantityByWarehouse(1L))
            .thenReturn(200);

    when(inventoryMapper.toEntity(request, warehouse, product))
            .thenReturn(inventory);

    when(inventoryRepository.save(any(WarehouseInventory.class)))
            .thenReturn(inventory);

    when(inventoryMapper.toResponse(inventory))
            .thenReturn(response);

    InventoryResponse result = inventoryService.createInventory(request);
    assertNotNull(result);
    assertEquals(100, result.getAvailableQuantity());
    verify(inventoryRepository).save(inventory);
}

@Test
void testCreateInventory_DuplicateInventory() {
    when(warehouseRepository.findByIdAndDeletedFalse(1L))
            .thenReturn(Optional.of(warehouse));

    when(productRepository.findByIdAndDeletedFalse(1L))
            .thenReturn(Optional.of(product));

    when(inventoryRepository.existsByWarehouseAndProduct(warehouse, product))
            .thenReturn(true);

    DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
            () -> inventoryService.createInventory(request));

    assertEquals("Inventory already exists for this warehouse and product.",exception.getMessage());
    verify(inventoryRepository, never()).save(any());
}

@Test
void testCreateInventory_WarehouseNotFound() {
    when(warehouseRepository.findByIdAndDeletedFalse(1L))
            .thenReturn(Optional.empty());

    assertThrows(WarehouseNotFoundException.class,
            () -> inventoryService.createInventory(request));

    verify(productRepository, never()).findByIdAndDeletedFalse(anyLong());
}

@Test
void testCreateInventory_ProductNotFound() {
    when(warehouseRepository.findByIdAndDeletedFalse(1L))
            .thenReturn(Optional.of(warehouse));

    when(productRepository.findByIdAndDeletedFalse(1L))
            .thenReturn(Optional.empty());

    assertThrows(ProductNotFoundException.class,() -> inventoryService.createInventory(request));
    verify(inventoryRepository, never()).save(any());
}

@Test
void testCreateInventory_WarehouseInactive() {
    warehouse.setStatus(WarehouseStatus.INACTIVE);

    when(warehouseRepository.findByIdAndDeletedFalse(1L))
            .thenReturn(Optional.of(warehouse));

    when(productRepository.findByIdAndDeletedFalse(1L))
            .thenReturn(Optional.of(product));

    InvalidRequestException exception = assertThrows(InvalidRequestException.class,
            () -> inventoryService.createInventory(request));

    assertEquals("Warehouse is inactive.",exception.getMessage());
    verify(inventoryRepository, never()).save(any());
}

@Test
void testCreateInventory_NegativeQuantity() {
    request.setAvailableQuantity(-10);

    when(warehouseRepository.findByIdAndDeletedFalse(1L))
            .thenReturn(Optional.of(warehouse));

    when(productRepository.findByIdAndDeletedFalse(1L))
            .thenReturn(Optional.of(product));

    InvalidRequestException exception = assertThrows(InvalidRequestException.class,
            () -> inventoryService.createInventory(request));

    assertEquals("Quantity cannot be negative.",exception.getMessage());
    verify(inventoryRepository, never()).save(any());
}

@Test
void testCreateInventory_QuantityNull() {
    request.setAvailableQuantity(null);

    when(warehouseRepository.findByIdAndDeletedFalse(1L))
            .thenReturn(Optional.of(warehouse));

    when(productRepository.findByIdAndDeletedFalse(1L))
            .thenReturn(Optional.of(product));

    InvalidRequestException exception = assertThrows(InvalidRequestException.class,
            () -> inventoryService.createInventory(request));

    assertEquals("Quantity cannot be null.", exception.getMessage());
    verify(inventoryRepository, never()).save(any());
}

@Test
void testCreateInventory_CapacityExceeded() {
    request.setAvailableQuantity(900);

    when(warehouseRepository.findByIdAndDeletedFalse(1L))
            .thenReturn(Optional.of(warehouse));

    when(productRepository.findByIdAndDeletedFalse(1L))
            .thenReturn(Optional.of(product));

    when(inventoryRepository.existsByWarehouseAndProduct(warehouse, product))
            .thenReturn(false);

    when(inventoryRepository.getTotalQuantityByWarehouse(1L))
            .thenReturn(200);

    WarehouseCapacityExceededException exception = assertThrows(WarehouseCapacityExceededException.class,
            () -> inventoryService.createInventory(request));

    assertEquals("Warehouse capacity exceeded.", exception.getMessage());
    verify(inventoryRepository, never()).save(any());
}

@Test
void testUpdateInventory_Success() {
    request.setAvailableQuantity(150);

    when(inventoryRepository.findById(1L))
            .thenReturn(Optional.of(inventory));

    when(inventoryRepository.getTotalQuantityByWarehouse(1L))
    .thenReturn(300);

    when(inventoryRepository.save(any(WarehouseInventory.class)))
            .thenReturn(inventory);

    when(inventoryMapper.toResponse(inventory))
            .thenReturn(response);

    InventoryResponse result =inventoryService.updateInventory(1L, request);

    assertNotNull(result);
    verify(inventoryRepository).findById(1L);
    verify(inventoryRepository).save(inventory);
    assertEquals(150, inventory.getAvailableQuantity());
}

@Test
void testUpdateInventory_NotFound() {
    when(inventoryRepository.findById(1L))
            .thenReturn(Optional.empty());

    InventoryNotFoundException exception = assertThrows(InventoryNotFoundException.class,
            () -> inventoryService.updateInventory(1L, request));

    assertEquals("Inventory not found with id : 1",exception.getMessage());
    verify(inventoryRepository, never()).save(any());
}

@Test
void testGetInventoryById() {
    when(inventoryRepository.findById(1L))
            .thenReturn(Optional.of(inventory));

    when(inventoryMapper.toResponse(inventory))
            .thenReturn(response);

    InventoryResponse result =inventoryService.getInventoryById(1L);
    assertNotNull(result);
    assertEquals(1L, result.getId());
    verify(inventoryRepository).findById(1L);
}

@Test
void testDeleteInventory() {
    when(inventoryRepository.findById(1L))
            .thenReturn(Optional.of(inventory));

    inventoryService.deleteInventory(1L);

    verify(inventoryRepository).findById(1L);
    verify(inventoryRepository).delete(inventory);
}

@Test
void testDeleteInventory_NotFound() {
    when(inventoryRepository.findById(1L))
            .thenReturn(Optional.empty());

    assertThrows(InventoryNotFoundException.class,() -> inventoryService.deleteInventory(1L));
    verify(inventoryRepository, never()).delete(any());
}

@Test
void testGetAllInventory() {
    Pageable pageable =PageRequest.of(0, 10, Sort.by("id").ascending());
    Page<WarehouseInventory> page =new PageImpl<>(List.of(inventory));

    when(inventoryRepository.findAll(pageable))
            .thenReturn(page);

    when(inventoryMapper.toResponse(any(WarehouseInventory.class)))
            .thenReturn(response);

    Page<InventoryResponse> result =inventoryService.getAllInventory(0,10,"id","ASC");                
    assertEquals(1, result.getTotalElements());
    verify(inventoryRepository).findAll(pageable);
}

@Test
void testGetInventoryByWarehouse() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<WarehouseInventory> page =new PageImpl<>(List.of(inventory));

    when(inventoryRepository.findByWarehouseId(1L, pageable))
            .thenReturn(page);

    when(inventoryMapper.toResponse(any(WarehouseInventory.class)))
            .thenReturn(response);

    Page<InventoryResponse> result =inventoryService.getInventoryByWarehouse(1L, 0, 10);
    assertEquals(1, result.getTotalElements());
    verify(inventoryRepository).findByWarehouseId(1L, pageable);
}

@Test
void testGetInventoryByProduct() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<WarehouseInventory> page =new PageImpl<>(List.of(inventory));

    when(inventoryRepository.findByProductId(1L, pageable))
            .thenReturn(page);

    when(inventoryMapper.toResponse(any(WarehouseInventory.class)))
            .thenReturn(response);

    Page<InventoryResponse> result =inventoryService.getInventoryByProduct(1L, 0, 10);
    assertEquals(1, result.getTotalElements());
    verify(inventoryRepository).findByProductId(1L, pageable);
}
}
