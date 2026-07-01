package com.warehouse.mapper;

import org.springframework.stereotype.Component;

import com.warehouse.entity.Allocation;
import com.warehouse.responsedto.AllocationResponse;

@Component
public class AllocationMapper {

    public AllocationResponse toResponse(Allocation allocation) {
        return AllocationResponse.builder()
                .allocationId(allocation.getId())
                .warehouseId(allocation.getWarehouse().getId())
                .warehouseName(allocation.getWarehouse().getName())
                .productId(allocation.getProduct().getId())
                .productName(allocation.getProduct().getName())
                .allocatedQuantity(allocation.getQuantity())
                .status(allocation.getStatus())
                .allocatedAt(allocation.getAllocatedAt())
                .build();

    }

}