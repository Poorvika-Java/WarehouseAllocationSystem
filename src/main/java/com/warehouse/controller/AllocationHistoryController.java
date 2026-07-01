package com.warehouse.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.responsedto.AllocationHistoryResponse;
import com.warehouse.responsedto.ApiResponse;
import com.warehouse.service.AllocationHistoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/allocation-history")
@Tag(name = "Allocation History APIs", description = "Allocation History APIs")
@RequiredArgsConstructor
public class AllocationHistoryController {

    private final AllocationHistoryService historyService;
    
 @GetMapping("/{id}")
 @Operation(summary = "Get Allocation History By Id")
    public ResponseEntity<ApiResponse<AllocationHistoryResponse>>getHistoryById(@PathVariable Long id) {
        AllocationHistoryResponse response =historyService.getHistoryById(id);
        return ResponseEntity.ok(ApiResponse.<AllocationHistoryResponse>builder()
                        .success(true)
                        .message("Allocation history fetched successfully")
                        .data(response)
                        .build());

    }
 
 @GetMapping
 @Operation(summary = "Get All Allocation History")
 public ResponseEntity<ApiResponse<Page<AllocationHistoryResponse>>>getAllHistory(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size,
         @RequestParam(defaultValue = "createdAt") String sortBy,@RequestParam(defaultValue = "DESC") String direction) {

     Page<AllocationHistoryResponse> response =historyService.getAllHistory(page,size,sortBy,direction);
     return ResponseEntity.ok(ApiResponse.<Page<AllocationHistoryResponse>>builder()
                     .success(true)
                     .message("Allocation history fetched successfully")
                     .data(response)
                     .build());

 }
 
 @GetMapping("/product/{productId}")
 @Operation(summary = "Get Allocation History By Product")
 public ResponseEntity<ApiResponse<Page<AllocationHistoryResponse>>>getHistoryByProduct(@PathVariable Long productId,@RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size) {

     Page<AllocationHistoryResponse> response =historyService.getHistoryByProduct(productId,page,size);
     return ResponseEntity.ok(ApiResponse.<Page<AllocationHistoryResponse>>builder()
                     .success(true)
                     .message("Allocation history fetched successfully")
                     .data(response)
                     .build());

 }
 
 @GetMapping("/warehouse/{warehouseId}")
 @Operation(summary = "Get Allocation History By Warehouse")
 public ResponseEntity<ApiResponse<Page<AllocationHistoryResponse>>>getHistoryByWarehouse(@PathVariable Long warehouseId,@RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size) {

     Page<AllocationHistoryResponse> response =historyService.getHistoryByWarehouse(warehouseId,page,size);
     return ResponseEntity.ok(ApiResponse.<Page<AllocationHistoryResponse>>builder()
                     .success(true)
                     .message("Allocation history fetched successfully")
                     .data(response)
                     .build());

 }

}