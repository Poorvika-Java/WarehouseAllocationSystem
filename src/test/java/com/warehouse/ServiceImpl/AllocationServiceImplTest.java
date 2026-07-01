package com.warehouse.ServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import com.warehouse.entity.Allocation;
import com.warehouse.entity.AllocationHistory;
import com.warehouse.entity.Product;
import com.warehouse.entity.Warehouse;
import com.warehouse.entity.WarehouseInventory;
import com.warehouse.enums.AllocationStatus;
import com.warehouse.enums.WarehouseStatus;
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
import com.warehouse.serviceimpl.AllocationServiceImpl;

@ExtendWith(MockitoExtension.class)
class AllocationServiceImplTest {

    @Mock
    private AllocationRepository allocationRepository;

    @Mock
    private WarehouseInventoryRepository inventoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AllocationHistoryRepository historyRepository;

    @Mock
    private AllocationMapper allocationMapper;

    @InjectMocks
    private AllocationServiceImpl allocationService;

    private Product product;
    private Warehouse warehouse;
    private WarehouseInventory inventory;
    private Allocation allocation;
    private AllocationRequest request;
    private AllocationResponse response;

    @BeforeEach
    void setUp() {

        warehouse = Warehouse.builder()
                .id(1L)
                .name("Warehouse A")
                .capacity(1000)
                .status(WarehouseStatus.ACTIVE)
                .build();

        product = Product.builder()
                .id(1L)
                .name("Laptop")
                .sku("LAP100")
                .totalStock(200)
                .build();
        

        inventory = WarehouseInventory.builder()
                .id(1L)
                .warehouse(warehouse)
                .product(product)
                .availableQuantity(100)
                .build();

        allocation = Allocation.builder()
                .id(1L)
                .warehouse(warehouse)
                .product(product)
                .quantity(10)
                .allocatedAt(LocalDateTime.now())
                .status(AllocationStatus.ALLOCATED)
                .build();

        request = new AllocationRequest();
        request.setProductId(1L);
        request.setQuantity(10);

        response = AllocationResponse.builder()
                .allocationId(1L)
                .warehouseId(1L)
                .warehouseName("Warehouse A")
                .productId(1L)
                .productName("Laptop")
                .allocatedQuantity(10)
                .status(AllocationStatus.ALLOCATED)
                .allocatedAt(LocalDateTime.now())
                .build();
    }
    
    @Test
    void testAllocateStock_Success() {
        when(productRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(product));

        when(inventoryRepository.findAvailableInventory(1L, 10))
                .thenReturn(List.of(inventory));

        when(inventoryRepository.save(any(WarehouseInventory.class)))
                .thenReturn(inventory);

        when(allocationRepository.save(any(Allocation.class)))
                .thenReturn(allocation);

        when(historyRepository.save(any(AllocationHistory.class)))
                .thenReturn(new AllocationHistory());

        when(allocationMapper.toResponse(allocation))
                .thenReturn(response);

        AllocationResponse result = allocationService.allocateStock(request);

        assertNotNull(result);
        assertEquals(10, result.getAllocatedQuantity());
        verify(inventoryRepository).save(inventory);
        verify(allocationRepository).save(any(Allocation.class));
        verify(historyRepository).save(any(AllocationHistory.class));
    }
    
    @Test
    void testAllocateStock_ProductNotFound() {
        when(productRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> allocationService.allocateStock(request));
        verify(inventoryRepository, never())
                .findAvailableInventory(anyLong(), anyInt());
    }
    
    @Test
    void testAllocateStock_NoWarehouseAvailable() {
        when(productRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(product));

        when(inventoryRepository.findAvailableInventory(1L, 10))
                .thenReturn(List.of());

        InsufficientStockException exception = assertThrows(InsufficientStockException.class,
                () -> allocationService.allocateStock(request));

        assertEquals("No warehouse has sufficient stock.",exception.getMessage());
        verify(allocationRepository, never()).save(any());
    }
    
    @Test
    void testAllocateStock_InsufficientStock() {
        inventory.setAvailableQuantity(5);

        when(productRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(product));

        when(inventoryRepository.findAvailableInventory(1L, 10))
                .thenReturn(List.of(inventory));

        InsufficientStockException exception = assertThrows(InsufficientStockException.class,
                () -> allocationService.allocateStock(request));

        assertEquals("No warehouse has sufficient stock.", exception.getMessage());
        verify(allocationRepository, never()).save(any());
    }
    
    @Test
    void testGetAllocationById() {
        when(allocationRepository.findById(1L))
                .thenReturn(Optional.of(allocation));

        when(allocationMapper.toResponse(allocation))
                .thenReturn(response);

        AllocationResponse result =allocationService.getAllocationById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getAllocationId());
        verify(allocationRepository).findById(1L);
    }
    
    @Test
    void testGetAllocationById_NotFound() {
        when(allocationRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(AllocationNotFoundException.class,
                () -> allocationService.getAllocationById(1L));
        verify(allocationMapper, never()).toResponse(any());
    }
    
    @Test
    void testGetAllAllocations() {
        Pageable pageable =PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Allocation> allocationPage =new PageImpl<>(List.of(allocation));

        when(allocationRepository.findAll(pageable))
                .thenReturn(allocationPage);

        when(allocationMapper.toResponse(any(Allocation.class)))
                .thenReturn(response);

        Page<AllocationResponse> result =allocationService.getAllAllocations(0,10,"id","ASC");                  
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(allocationRepository).findAll(pageable);
    }
    
    @Test
    void testGetAllocationByProduct() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Allocation> allocationPage =new PageImpl<>(List.of(allocation));

        when(allocationRepository.findByProductId(1L, pageable))
                .thenReturn(allocationPage);

        when(allocationMapper.toResponse(any(Allocation.class)))
                .thenReturn(response);

        Page<AllocationResponse> result =allocationService.getAllocationByProduct(1L, 0, 10);
        assertEquals(1, result.getTotalElements());
        verify(allocationRepository)
                .findByProductId(1L, pageable);
    }
    
    @Test
    void testGetAllocationByWarehouse() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Allocation> allocationPage =new PageImpl<>(List.of(allocation));

        when(allocationRepository.findByWarehouseId(1L, pageable))
                .thenReturn(allocationPage);

        when(allocationMapper.toResponse(any(Allocation.class)))
                .thenReturn(response);

        Page<AllocationResponse> result =allocationService.getAllocationByWarehouse(1L, 0, 10);
        assertEquals(1, result.getTotalElements());
        verify(allocationRepository)
                .findByWarehouseId(1L, pageable);
    }
    
    @Test
    void testGetAllocationByDateRange() {
        LocalDateTime from = LocalDateTime.now().minusDays(5);
        LocalDateTime to = LocalDateTime.now();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Allocation> allocationPage =new PageImpl<>(List.of(allocation));

        when(allocationRepository.findByAllocatedAtBetween(from, to, pageable))
                .thenReturn(allocationPage);

        when(allocationMapper.toResponse(any(Allocation.class)))
                .thenReturn(response);

        Page<AllocationResponse> result =allocationService.getAllocationByDateRange(from,to,0,10);                     
        assertEquals(1, result.getTotalElements());
        verify(allocationRepository)
                .findByAllocatedAtBetween(from, to, pageable);
    }
    
    @Test
    void testAllocateStock_SelectWarehouseWithHighestStock() {
        WarehouseInventory inventory1 = WarehouseInventory.builder()
                .id(1L)
                .warehouse(warehouse)
                .product(product)
                .availableQuantity(50)
                .build();

        WarehouseInventory inventory2 = WarehouseInventory.builder()
                .id(2L)
                .warehouse(warehouse)
                .product(product)
                .availableQuantity(200)
                .build();

        when(productRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(product));

        when(inventoryRepository.findAvailableInventory(1L, 10))
                .thenReturn(List.of(inventory1, inventory2));

        when(inventoryRepository.save(any(WarehouseInventory.class)))
                .thenReturn(inventory2);

        when(allocationRepository.save(any(Allocation.class)))
                .thenReturn(allocation);

        when(historyRepository.save(any(AllocationHistory.class)))
                .thenReturn(new AllocationHistory());

        when(allocationMapper.toResponse(any(Allocation.class)))
                .thenReturn(response);

        AllocationResponse result =allocationService.allocateStock(request);
        assertNotNull(result);
        verify(inventoryRepository).save(inventory2);
    }
    
    @Test
    void testAllocateStock_SaveHistory() {
        when(productRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(product));

        when(inventoryRepository.findAvailableInventory(1L, 10))
                .thenReturn(List.of(inventory));

        when(inventoryRepository.save(any()))
                .thenReturn(inventory);

        when(allocationRepository.save(any()))
                .thenReturn(allocation);

        when(historyRepository.save(any()))
                .thenReturn(new AllocationHistory());

        when(allocationMapper.toResponse(any()))
                .thenReturn(response);

        allocationService.allocateStock(request);
        verify(historyRepository, times(1))
                .save(any(AllocationHistory.class));
    }
}