package com.warehouse.responsedto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InventoryResponse {

    private Long id;

    private Long warehouseId;

    private String warehouseName;

    private Long productId;

    private String productName;

    private String sku; //Stock Keeping Unit

    private Integer availableQuantity;

}