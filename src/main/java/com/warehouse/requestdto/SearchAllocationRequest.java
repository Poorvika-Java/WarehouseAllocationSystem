package com.warehouse.requestdto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SearchAllocationRequest {

    private Long warehouseId;

    private Long productId;

    private LocalDate startDate;

    private LocalDate endDate;

}