package com.warehouse.mapper;

import org.springframework.stereotype.Component;

import com.warehouse.entity.StockTransfer;
import com.warehouse.responsedto.StockTransferResponse;

@Component
public class StockTransferMapper {

    public StockTransferResponse toResponse(StockTransfer transfer) {

        return StockTransferResponse.builder()
                .transferId(transfer.getId())
                .sourceWarehouseId(transfer.getSourceWarehouse().getId())
                .sourceWarehouseName(transfer.getSourceWarehouse().getName())
                .targetWarehouseId(transfer.getTargetWarehouse().getId())
                .targetWarehouseName(transfer.getTargetWarehouse().getName())
                .productId(transfer.getProduct().getId())
                .productName(transfer.getProduct().getName())
                .quantity(transfer.getQuantity())
                .transferDate(transfer.getTransferDate())
                .build();

    }

}