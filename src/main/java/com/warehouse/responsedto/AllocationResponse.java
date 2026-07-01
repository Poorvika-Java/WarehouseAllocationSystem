package com.warehouse.responsedto;

import java.time.LocalDateTime;

import com.warehouse.enums.AllocationStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AllocationResponse {

    private Long allocationId;

    private Long warehouseId;

    private String warehouseName;

    private Long productId;

    private String productName;

    private Integer allocatedQuantity;

    private AllocationStatus status;

    private LocalDateTime allocatedAt;

}