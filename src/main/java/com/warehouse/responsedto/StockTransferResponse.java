package com.warehouse.responsedto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockTransferResponse {

    private Long transferId;

    private Long sourceWarehouseId;

    private String sourceWarehouseName;

    private Long targetWarehouseId;

    private String targetWarehouseName;

    private Long productId;

    private String productName;

    private Integer quantity;

    private LocalDateTime transferDate;

}