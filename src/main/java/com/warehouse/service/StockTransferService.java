package com.warehouse.service;

import org.springframework.data.domain.Page;
import com.warehouse.requestdto.StockTransferRequest;
import com.warehouse.responsedto.StockTransferResponse;

public interface StockTransferService {

    StockTransferResponse transferStock(StockTransferRequest request);

    StockTransferResponse getTransferById(Long id);

    Page<StockTransferResponse> getAllTransfers(int page,int size,String sortBy,String direction);

}