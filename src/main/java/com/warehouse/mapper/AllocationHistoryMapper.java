package com.warehouse.mapper;

import org.springframework.stereotype.Component;

import com.warehouse.entity.AllocationHistory;
import com.warehouse.responsedto.AllocationHistoryResponse;

@Component
public class AllocationHistoryMapper {

    public AllocationHistoryResponse toResponse(
            AllocationHistory history) {

        return AllocationHistoryResponse.builder()
                .historyId(history.getId())
                .allocationId(history.getAllocation().getId())
                .productId(history.getAllocation().getProduct().getId())
                .productName(history.getAllocation().getProduct().getName())
                .warehouseId(history.getAllocation().getWarehouse().getId())
                .warehouseName(history.getAllocation().getWarehouse().getName())
                .quantity(history.getAllocation().getQuantity())
                .status(history.getStatus())
                .remarks(history.getRemarks())
                .createdAt(history.getCreatedAt())
                .build();

    }

}