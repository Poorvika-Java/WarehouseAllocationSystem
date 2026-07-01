package com.warehouse.responsedto;

import java.time.LocalDateTime;

import com.warehouse.enums.AllocationStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AllocationHistoryResponse {

    private Long historyId;

    private Long allocationId;

    private Long productId;

    private String productName;

    private Long warehouseId;

    private String warehouseName;

    private Integer quantity;

    private AllocationStatus status;

    private String remarks;

    private LocalDateTime createdAt;

}