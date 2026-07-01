package com.warehouse.ServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
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
import com.warehouse.entity.*;
import com.warehouse.enums.AllocationStatus;
import com.warehouse.exception.AllocationHistoryNotFoundException;
import com.warehouse.mapper.AllocationHistoryMapper;
import com.warehouse.repository.AllocationHistoryRepository;
import com.warehouse.responsedto.AllocationHistoryResponse;
import com.warehouse.serviceimpl.AllocationHistoryServiceImpl;

@ExtendWith(MockitoExtension.class)
class AllocationHistoryServiceImplTest {

    @Mock
    private AllocationHistoryRepository historyRepository;

    @Mock
    private AllocationHistoryMapper historyMapper;

    @InjectMocks
    private AllocationHistoryServiceImpl historyService;

    private AllocationHistory history;
    private AllocationHistoryResponse response;

    @BeforeEach
    void setUp() {

        Warehouse warehouse = Warehouse.builder()
                .id(1L)
                .name("Warehouse A")
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("Laptop")
                .build();

        Allocation allocation = Allocation.builder()
                .id(1L)
                .warehouse(warehouse)
                .product(product)
                .quantity(20)
                .build();

        history = AllocationHistory.builder()
                .id(1L)
                .allocation(allocation)
                .status(AllocationStatus.ALLOCATED)
                .remarks("Stock Allocated")
                .createdAt(LocalDateTime.now())
                .build();

        response = AllocationHistoryResponse.builder()
                .historyId(1L)
                .allocationId(1L)
                .productId(1L)
                .warehouseId(1L)
                .quantity(20)
                .status(AllocationStatus.ALLOCATED)
                .remarks("Stock Allocated")
                .createdAt(LocalDateTime.now())
                .build();
    }
    
    @Test
    void testGetHistoryById() {
        when(historyRepository.findById(1L))
                .thenReturn(Optional.of(history));

        when(historyMapper.toResponse(history))
                .thenReturn(response);

        AllocationHistoryResponse result =historyService.getHistoryById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getHistoryId());
        verify(historyRepository).findById(1L);
    }
    
    @Test
    void testGetHistoryById_NotFound() {
        when(historyRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(AllocationHistoryNotFoundException.class,
                () -> historyService.getHistoryById(1L));
    }
    
    @Test
    void testGetAllHistory() {
        Pageable pageable =PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<AllocationHistory> page =new PageImpl<>(List.of(history));

        when(historyRepository.findAll(pageable))
                .thenReturn(page);

        when(historyMapper.toResponse(any()))
                .thenReturn(response);

        Page<AllocationHistoryResponse> result =historyService.getAllHistory(0,10,"id","ASC");                   
        assertEquals(1, result.getTotalElements());
    }
    
    @Test
    void testGetHistoryByProduct() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<AllocationHistory> page =new PageImpl<>(List.of(history));

        when(historyRepository.findByAllocationProductId(1L, pageable))
                .thenReturn(page);

        when(historyMapper.toResponse(any()))
                .thenReturn(response);

        Page<AllocationHistoryResponse> result =historyService.getHistoryByProduct(1L, 0, 10);
        assertEquals(1, result.getTotalElements());
    }
    
    @Test
    void testGetHistoryByWarehouse() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<AllocationHistory> page =new PageImpl<>(List.of(history));

        when(historyRepository.findByAllocationWarehouseId(1L, pageable))
                .thenReturn(page);

        when(historyMapper.toResponse(any()))
                .thenReturn(response);

        Page<AllocationHistoryResponse> result =historyService.getHistoryByWarehouse(1L, 0, 10);
        assertEquals(1, result.getTotalElements());
    }
}
