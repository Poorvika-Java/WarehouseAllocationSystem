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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.warehouse.entity.Warehouse;
import com.warehouse.enums.WarehouseStatus;
import com.warehouse.exception.DuplicateResourceException;
import com.warehouse.exception.WarehouseNotFoundException;
import com.warehouse.mapper.WarehouseMapper;
import com.warehouse.repository.WarehouseRepository;
import com.warehouse.requestdto.WarehouseRequest;
import com.warehouse.responsedto.WarehouseResponse;
import com.warehouse.serviceimpl.WarehouseServiceImpl;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceImplTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private WarehouseMapper warehouseMapper;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    private Warehouse warehouse;
    private WarehouseRequest request;
    private WarehouseResponse response;

    @BeforeEach
    void setUp() {

        warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("Warehouse A");
        warehouse.setLocation("Bangalore");
        warehouse.setCapacity(1000);
        warehouse.setStatus(WarehouseStatus.ACTIVE);
        warehouse.setDeleted(false);
        warehouse.setCreatedAt(LocalDateTime.now());
        warehouse.setUpdatedAt(LocalDateTime.now());

        request = new WarehouseRequest();
        request.setName("Warehouse A");
        request.setLocation("Bangalore");
        request.setCapacity(1000);
        request.setStatus(WarehouseStatus.ACTIVE);

        response = WarehouseResponse.builder()
                .id(1L)
                .name("Warehouse A")
                .location("Bangalore")
                .capacity(1000)
                .status(WarehouseStatus.ACTIVE)
                .createdAt(warehouse.getCreatedAt())
                .updatedAt(warehouse.getUpdatedAt())
                .build();
    }
    
    @Test
    void testCreateWarehouse_Success() {
        when(warehouseRepository.existsByNameIgnoreCase("Warehouse A"))
                .thenReturn(false);
        when(warehouseMapper.toEntity(request))
                .thenReturn(warehouse);
        when(warehouseRepository.save(any(Warehouse.class)))
                .thenReturn(warehouse);
        when(warehouseMapper.toResponse(warehouse))
                .thenReturn(response);

        WarehouseResponse result = warehouseService.createWarehouse(request);
        assertNotNull(result);
        assertEquals("Warehouse A", result.getName());
        assertEquals("Bangalore", result.getLocation());
        assertEquals(1000, result.getCapacity());

        verify(warehouseRepository, times(1))
                .existsByNameIgnoreCase("Warehouse A");

        verify(warehouseMapper, times(1))
                .toEntity(request);

        verify(warehouseRepository, times(1))
                .save(warehouse);

        verify(warehouseMapper, times(1))
                .toResponse(warehouse);
    }
    
    @Test
    void testCreateWarehouse_DuplicateName() {
        when(warehouseRepository.existsByNameIgnoreCase("Warehouse A"))
                .thenReturn(true);
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> warehouseService.createWarehouse(request));

        assertEquals("Warehouse already exists.", exception.getMessage());
        verify(warehouseRepository, times(1))
                .existsByNameIgnoreCase("Warehouse A");

        verify(warehouseRepository, never())
                .save(any(Warehouse.class));

        verify(warehouseMapper, never())
                .toEntity(any(WarehouseRequest.class));

        verify(warehouseMapper, never())
                .toResponse(any(Warehouse.class));
    }
    
    @Test
    void testGetWarehouseById_Success() {
        when(warehouseRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(warehouse));
        when(warehouseMapper.toResponse(warehouse))
                .thenReturn(response);

        WarehouseResponse result = warehouseService.getWarehouseById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Warehouse A", result.getName());
        assertEquals("Bangalore", result.getLocation());

        verify(warehouseRepository, times(1))
                .findByIdAndDeletedFalse(1L);

        verify(warehouseMapper, times(1))
                .toResponse(warehouse);
    }
    
    @Test
    void testUpdateWarehouse_Success() {
        WarehouseRequest updateRequest = new WarehouseRequest();
        updateRequest.setName("Updated Warehouse");
        updateRequest.setLocation("Hyderabad");
        updateRequest.setCapacity(2000);
        updateRequest.setStatus(WarehouseStatus.ACTIVE);

        warehouse.setName("Updated Warehouse");
        warehouse.setLocation("Hyderabad");
        warehouse.setCapacity(2000);

        WarehouseResponse updatedResponse = WarehouseResponse.builder()
                .id(1L)
                .name("Updated Warehouse")
                .location("Hyderabad")
                .capacity(2000)
                .status(WarehouseStatus.ACTIVE)
                .createdAt(warehouse.getCreatedAt())
                .updatedAt(warehouse.getUpdatedAt())
                .build();

        when(warehouseRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(warehouse));

        when(warehouseRepository.save(any(Warehouse.class)))
                .thenReturn(warehouse);

        when(warehouseMapper.toResponse(warehouse))
                .thenReturn(updatedResponse);

        WarehouseResponse result =
                warehouseService.updateWarehouse(1L, updateRequest);

        assertNotNull(result);
        assertEquals("Updated Warehouse", result.getName());
        assertEquals("Hyderabad", result.getLocation());
        assertEquals(2000, result.getCapacity());

        verify(warehouseRepository).findByIdAndDeletedFalse(1L);
        verify(warehouseRepository).save(warehouse);
        verify(warehouseMapper).toResponse(warehouse);
    }
    
    @Test
    void testUpdateWarehouse_NotFound() {
        when(warehouseRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.empty());

        WarehouseNotFoundException exception = assertThrows(
                WarehouseNotFoundException.class,
                () -> warehouseService.updateWarehouse(1L, request));

        assertEquals("Warehouse not found with id : 1",exception.getMessage());

        verify(warehouseRepository).findByIdAndDeletedFalse(1L);
        verify(warehouseRepository, never()).save(any());
    }
    
    @Test
    void testActivateWarehouse() {
        warehouse.setStatus(WarehouseStatus.INACTIVE);

        when(warehouseRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(warehouse));

        when(warehouseRepository.save(any(Warehouse.class)))
                .thenReturn(warehouse);

        when(warehouseMapper.toResponse(warehouse))
                .thenReturn(response);

        WarehouseResponse result =warehouseService.activateWarehouse(1L);
        assertNotNull(result);

        verify(warehouseRepository).findByIdAndDeletedFalse(1L);
        verify(warehouseRepository).save(warehouse);
        verify(warehouseMapper).toResponse(warehouse);

        assertEquals(WarehouseStatus.ACTIVE,warehouse.getStatus());
    }
    
    @Test
    void testDeactivateWarehouse() {
        warehouse.setStatus(WarehouseStatus.ACTIVE);

        when(warehouseRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(warehouse));

        when(warehouseRepository.save(any(Warehouse.class)))
                .thenReturn(warehouse);

        when(warehouseMapper.toResponse(warehouse))
                .thenReturn(response);

        WarehouseResponse result =warehouseService.deactivateWarehouse(1L);
        assertNotNull(result);
        verify(warehouseRepository).findByIdAndDeletedFalse(1L);
        verify(warehouseRepository).save(warehouse);
        verify(warehouseMapper).toResponse(warehouse);

        assertEquals(WarehouseStatus.INACTIVE,warehouse.getStatus());
    }
    
    @Test
    void testDeleteWarehouse() {
        when(warehouseRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(warehouse));

        when(warehouseRepository.save(any(Warehouse.class)))
                .thenReturn(warehouse);

        warehouseService.deleteWarehouse(1L);

        verify(warehouseRepository).findByIdAndDeletedFalse(1L);
        verify(warehouseRepository).save(warehouse);
        assertTrue(warehouse.getDeleted());
        assertNotNull(warehouse.getDeletedAt());
    }
    
    @Test
    void testDeleteWarehouse_NotFound() {
        when(warehouseRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.empty());

        assertThrows(WarehouseNotFoundException.class,
                () -> warehouseService.deleteWarehouse(1L));

        verify(warehouseRepository).findByIdAndDeletedFalse(1L);
        verify(warehouseRepository, never()).save(any());
    }
    
    @Test
    void testGetAllWarehouses() {
        Page<Warehouse> warehousePage = new PageImpl<>(List.of(warehouse));

        when(warehouseRepository.findByDeletedFalse(any(Pageable.class)))
                .thenReturn(warehousePage);

        when(warehouseMapper.toResponse(any(Warehouse.class)))
                .thenReturn(response);

        Page<WarehouseResponse> result =warehouseService.getAllWarehouses(0, 10, "id", "ASC");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Warehouse A", result.getContent().get(0).getName());
        verify(warehouseRepository).findByDeletedFalse(any(Pageable.class));
        verify(warehouseMapper).toResponse(any(Warehouse.class));
    }
}