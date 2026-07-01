package com.warehouse.serviceimpl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.warehouse.entity.Warehouse;
import com.warehouse.enums.WarehouseStatus;
import com.warehouse.exception.DuplicateResourceException;
import com.warehouse.exception.WarehouseNotFoundException;
import com.warehouse.mapper.WarehouseMapper;
import com.warehouse.repository.WarehouseRepository;
import com.warehouse.requestdto.WarehouseRequest;
import com.warehouse.responsedto.WarehouseResponse;
import com.warehouse.service.WarehouseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private static final Logger log =LoggerFactory.getLogger(WarehouseServiceImpl.class);
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    @Override
    public WarehouseResponse createWarehouse(WarehouseRequest request) {
        log.info("Creating warehouse : {}", request.getName());

        if (warehouseRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Warehouse already exists.");
        }

        Warehouse warehouse = warehouseMapper.toEntity(request);
        warehouse = warehouseRepository.save(warehouse);
        log.info("Warehouse created successfully : {}", warehouse.getId());
        return warehouseMapper.toResponse(warehouse);

    }

    @Override
    public WarehouseResponse updateWarehouse(Long id,WarehouseRequest request) {
        log.info("Updating warehouse : {}", id);

        Warehouse warehouse = warehouseRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new WarehouseNotFoundException("Warehouse not found with id : " + id));

        warehouse.setName(request.getName());
        warehouse.setLocation(request.getLocation());
        warehouse.setCapacity(request.getCapacity());
        warehouse.setStatus(request.getStatus());
        warehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toResponse(warehouse);

    }

    @Override
    @Transactional(readOnly = true)
    public WarehouseResponse getWarehouseById(Long id) {
        Warehouse warehouse = warehouseRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new WarehouseNotFoundException("Warehouse not found with id : " + id));

        return warehouseMapper.toResponse(warehouse);

    }

    @Override
    public Page<WarehouseResponse> getAllWarehouses(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return warehouseRepository.findByDeletedFalse(pageable)
                .map(warehouseMapper::toResponse);
    }
    
    @Override
    public WarehouseResponse activateWarehouse(Long id) {
        Warehouse warehouse = warehouseRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new WarehouseNotFoundException("Warehouse not found with id : " + id));

        warehouse.setStatus(WarehouseStatus.ACTIVE);
        warehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toResponse(warehouse);

    }

    @Override
    public WarehouseResponse deactivateWarehouse(Long id) {
        Warehouse warehouse = warehouseRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new WarehouseNotFoundException("Warehouse not found with id : " + id));

        warehouse.setStatus(WarehouseStatus.INACTIVE);
        warehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toResponse(warehouse);

    }

    @Override
    public void deleteWarehouse(Long id) {
        Warehouse warehouse = warehouseRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new WarehouseNotFoundException("Warehouse not found with id : " + id));

        warehouse.setDeleted(true);
        warehouse.setDeletedAt(LocalDateTime.now());
        warehouseRepository.save(warehouse);
        log.info("Warehouse Soft Deleted : {}", id);

    }

}