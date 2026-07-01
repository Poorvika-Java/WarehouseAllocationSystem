package com.warehouse.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.warehouse.requestdto.AllocationRequest;
import com.warehouse.responsedto.AllocationResponse;

public interface AllocationService {

    AllocationResponse allocateStock(AllocationRequest request);

    AllocationResponse getAllocationById(Long id);

    Page<AllocationResponse> getAllAllocations(int page,int size,String sortBy,String direction);

    Page<AllocationResponse> getAllocationByProduct(Long productId,int page,int size);

    Page<AllocationResponse> getAllocationByWarehouse(Long warehouseId,int page,int size);
    
    Page<AllocationResponse> getAllocationByDateRange(LocalDateTime from,LocalDateTime to,int page,int size);

}