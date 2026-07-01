package com.warehouse.service;

import org.springframework.data.domain.Page;

import com.warehouse.requestdto.InventoryRequest;
import com.warehouse.responsedto.InventoryResponse;

public interface WarehouseInventoryService {

    InventoryResponse createInventory(InventoryRequest request);

    InventoryResponse updateInventory(Long id,InventoryRequest request);

    InventoryResponse getInventoryById(Long id);

    Page<InventoryResponse> getAllInventory(int page,int size,String sortBy,String direction);

    Page<InventoryResponse> getInventoryByWarehouse(Long warehouseId,int page,int size);

    Page<InventoryResponse> getInventoryByProduct(Long productId,int page,int size);

    void deleteInventory(Long id);

}