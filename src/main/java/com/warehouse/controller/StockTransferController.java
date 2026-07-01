package com.warehouse.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.warehouse.requestdto.StockTransferRequest;
import com.warehouse.responsedto.StockTransferResponse;
import com.warehouse.service.StockTransferService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stock-transfers")
@Tag(name = "Stock Transfer APIs", description = "Stock Transfer Management APIs")
@RequiredArgsConstructor
public class StockTransferController {

    private final StockTransferService stockTransferService;

    @PostMapping
    public StockTransferResponse transferStock(@RequestBody StockTransferRequest request) {
        return stockTransferService.transferStock(request);
    }

    @GetMapping("/{id}")
    public StockTransferResponse getTransferById(@PathVariable Long id) {
        return stockTransferService.getTransferById(id);
    }

    @GetMapping
    public Page<StockTransferResponse> getAllTransfers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        return stockTransferService.getAllTransfers(page, size, sortBy, direction);
    }
}