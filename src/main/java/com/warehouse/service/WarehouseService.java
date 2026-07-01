package com.warehouse.service;

import org.springframework.data.domain.Page;

import com.warehouse.requestdto.WarehouseRequest;
import com.warehouse.responsedto.WarehouseResponse;

public interface WarehouseService {

    WarehouseResponse createWarehouse(WarehouseRequest request);

    WarehouseResponse updateWarehouse(Long id, WarehouseRequest request);

    WarehouseResponse getWarehouseById(Long id);

    Page<WarehouseResponse> getAllWarehouses(int page,int size,String sortBy,String direction);

    WarehouseResponse activateWarehouse(Long id);

    WarehouseResponse deactivateWarehouse(Long id);

    void deleteWarehouse(Long id);

}