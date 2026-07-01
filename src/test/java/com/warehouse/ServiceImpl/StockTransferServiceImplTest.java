package com.warehouse.ServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import com.warehouse.entity.*;
import com.warehouse.exception.*;
import com.warehouse.mapper.StockTransferMapper;
import com.warehouse.repository.*;
import com.warehouse.requestdto.StockTransferRequest;
import com.warehouse.responsedto.StockTransferResponse;
import com.warehouse.serviceimpl.StockTransferServiceImpl;

@ExtendWith(MockitoExtension.class)
class StockTransferServiceImplTest {

    @Mock
    private StockTransferRepository transferRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private WarehouseInventoryRepository inventoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockTransferMapper transferMapper;

    @InjectMocks
    private StockTransferServiceImpl transferService;

    private Warehouse source;
    private Warehouse target;
    private Product product;
    private WarehouseInventory sourceInventory;
    private WarehouseInventory targetInventory;
    private StockTransfer transfer;
    private StockTransferRequest request;
    private StockTransferResponse response;

    @BeforeEach
    void setUp() {

        source = Warehouse.builder()
                .id(1L)
                .name("Source")
                .capacity(1000)
                .build();

        target = Warehouse.builder()
                .id(2L)
                .name("Target")
                .capacity(1000)
                .build();

        product = Product.builder()
                .id(1L)
                .name("Laptop")
                .sku("LAP100")
                .build();

        sourceInventory = WarehouseInventory.builder()
                .warehouse(source)
                .product(product)
                .availableQuantity(100)
                .build();

        targetInventory = WarehouseInventory.builder()
                .warehouse(target)
                .product(product)
                .availableQuantity(20)
                .build();

        transfer = StockTransfer.builder()
                .id(1L)
                .sourceWarehouse(source)
                .targetWarehouse(target)
                .product(product)
                .quantity(10)
                .transferDate(LocalDateTime.now())
                .build();

        request = new StockTransferRequest();
        request.setSourceWarehouseId(1L);
        request.setTargetWarehouseId(2L);
        request.setProductId(1L);
        request.setQuantity(10);

        response = StockTransferResponse.builder()
                .transferId(1L)
                .sourceWarehouseId(1L)
                .targetWarehouseId(2L)
                .productId(1L)
                .quantity(10)
                .transferDate(LocalDateTime.now())
                .build();
    }

    @Test
    void testTransferStock_Success() {
        when(warehouseRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(source));
        when(warehouseRepository.findByIdAndDeletedFalse(2L)).thenReturn(Optional.of(target));
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(product));

        when(inventoryRepository.findByWarehouseAndProduct(source, product))
                .thenReturn(Optional.of(sourceInventory));

        when(inventoryRepository.findByWarehouseAndProduct(target, product))
                .thenReturn(Optional.of(targetInventory));

        when(inventoryRepository.getTotalQuantityByWarehouse(2L))
                .thenReturn(100);

        when(transferRepository.save(any()))
                .thenReturn(transfer);

        when(transferMapper.toResponse(transfer))
                .thenReturn(response);

        StockTransferResponse result = transferService.transferStock(request);
        assertNotNull(result);
        verify(transferRepository).save(any(StockTransfer.class));
    }

    
    @Test
    void testTransferStock_SameWarehouse() {
        request.setTargetWarehouseId(1L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transferService.transferStock(request));

        assertEquals("Source and Target warehouse cannot be same.",exception.getMessage());
    }
    
    @Test
    void testTransferStock_SourceWarehouseNotFound() {
        when(warehouseRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.empty());

        assertThrows(WarehouseNotFoundException.class,() -> transferService.transferStock(request));
    }
    
    @Test
    void testTransferStock_ProductNotFound() {
        when(warehouseRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(source));

        when(warehouseRepository.findByIdAndDeletedFalse(2L))
                .thenReturn(Optional.of(target));

        when(productRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> transferService.transferStock(request));
    }
    
    @Test
    void testTransferStock_InsufficientStock() {
        sourceInventory.setAvailableQuantity(5);

        when(warehouseRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(source));
        when(warehouseRepository.findByIdAndDeletedFalse(2L)).thenReturn(Optional.of(target));
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(product));

        when(inventoryRepository.findByWarehouseAndProduct(source, product))
                .thenReturn(Optional.of(sourceInventory));

        assertThrows(InsufficientStockException.class,
                () -> transferService.transferStock(request));
    }
    
    @Test
    void testTransferStock_CapacityExceeded() {
        when(warehouseRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(source));
        when(warehouseRepository.findByIdAndDeletedFalse(2L)).thenReturn(Optional.of(target));
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(product));

        when(inventoryRepository.findByWarehouseAndProduct(source, product))
                .thenReturn(Optional.of(sourceInventory));

        when(inventoryRepository.findByWarehouseAndProduct(target, product))
                .thenReturn(Optional.of(targetInventory));

        when(inventoryRepository.getTotalQuantityByWarehouse(2L))
                .thenReturn(995);

        assertThrows(WarehouseCapacityExceededException.class,
                () -> transferService.transferStock(request));
    }
    
    @Test
    void testGetTransferById() {
        when(transferRepository.findById(1L))
                .thenReturn(Optional.of(transfer));

        when(transferMapper.toResponse(transfer))
                .thenReturn(response);

        StockTransferResponse result =transferService.getTransferById(1L);
        assertNotNull(result);
        verify(transferRepository).findById(1L);
    }
    
    @Test
    void testGetTransferById_NotFound() {
        when(transferRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(StockTransferNotFoundException.class,
                () -> transferService.getTransferById(1L));
    }
}
