package com.warehouse.requestdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "SKU is required") // Stock Keeping Unit
    private String sku;

    @NotNull(message = "Total stock is required")
    @Min(value = 0, message = "Total stock cannot be negative")
    private Integer totalStock;

}