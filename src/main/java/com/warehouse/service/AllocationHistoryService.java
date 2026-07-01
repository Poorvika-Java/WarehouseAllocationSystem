package com.warehouse.service;

import org.springframework.data.domain.Page;
import com.warehouse.responsedto.AllocationHistoryResponse;

public interface AllocationHistoryService {

    AllocationHistoryResponse getHistoryById(Long id);

    Page<AllocationHistoryResponse> getAllHistory(int page,int size,String sortBy,String direction);

    Page<AllocationHistoryResponse> getHistoryByProduct(Long productId,int page,int size);

    Page<AllocationHistoryResponse> getHistoryByWarehouse(Long warehouseId,int page,int size);

}