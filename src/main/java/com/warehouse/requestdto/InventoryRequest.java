package com.warehouse.requestdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class InventoryRequest {

    @NotNull(message = "Warehouse Id is required")
    private Long warehouseId;

    @NotNull(message = "Product Id is required")
    private Long productId;

    @NotNull(message = "Available Quantity is required")
    @Min(value = 0,message = "Available quantity cannot be negative")
    private Integer availableQuantity;

}