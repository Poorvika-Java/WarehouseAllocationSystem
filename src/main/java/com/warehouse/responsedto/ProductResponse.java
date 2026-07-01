package com.warehouse.responsedto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ProductResponse {

    private Long id;

    private String name;

    private String sku;

    private Integer totalStock;

}