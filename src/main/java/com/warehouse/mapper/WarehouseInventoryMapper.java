package com.warehouse.mapper;

import org.springframework.stereotype.Component;

import com.warehouse.entity.Product;
import com.warehouse.entity.Warehouse;
import com.warehouse.entity.WarehouseInventory;
import com.warehouse.requestdto.InventoryRequest;
import com.warehouse.responsedto.InventoryResponse;

@Component
public class WarehouseInventoryMapper {

    public WarehouseInventory toEntity(InventoryRequest request,Warehouse warehouse,Product product) {
        return WarehouseInventory.builder()
                .warehouse(warehouse)
                .product(product)
                .availableQuantity(request.getAvailableQuantity())
                .build();

    }

    public InventoryResponse toResponse(WarehouseInventory inventory) {
        return InventoryResponse.builder()
                .id(inventory.getId())
                .warehouseId(inventory.getWarehouse().getId())
                .warehouseName(inventory.getWarehouse().getName())
                .productId(inventory.getProduct().getId())
                .productName(inventory.getProduct().getName())
                .sku(inventory.getProduct().getSku())
                .availableQuantity(inventory.getAvailableQuantity())
                .build();

    }

}