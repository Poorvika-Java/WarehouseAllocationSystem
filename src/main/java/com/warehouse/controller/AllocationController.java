package com.warehouse.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.warehouse.requestdto.AllocationRequest;
import com.warehouse.responsedto.AllocationResponse;
import com.warehouse.responsedto.ApiResponse;
import com.warehouse.service.AllocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/allocations")
@Tag(name = "Allocation APIs", description = "Allocation Management APIs")
@RequiredArgsConstructor
public class AllocationController {

    private final AllocationService allocationService;
    
 @PostMapping
 @Operation(summary = "Allocate product to warehouse")
    public ResponseEntity<ApiResponse<AllocationResponse>> allocateStock(@Valid @RequestBody AllocationRequest request) {
        AllocationResponse response =allocationService.allocateStock(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<AllocationResponse>builder()
                                .success(true)
                                .message("Stock allocated successfully")
                                .data(response)
                                .build());
 }
 
 @GetMapping("/{id}")
 @Operation(summary = "Get Allocation By Id")
 public ResponseEntity<ApiResponse<AllocationResponse>> getAllocationById(@PathVariable Long id) {
     AllocationResponse response =allocationService.getAllocationById(id);
     return ResponseEntity.ok(ApiResponse.<AllocationResponse>builder()
    	                .success(true)
    	                .message("Allocation fetched successfully")
    	                .data(response)
    	                .build());
 }
     
 
 @GetMapping
 @Operation(summary = "Get All Allocations")
 public ResponseEntity<ApiResponse<Page<AllocationResponse>>> getAllAllocations(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size,
         @RequestParam(defaultValue = "allocatedAt") String sortBy, @RequestParam(defaultValue = "DESC") String direction) {

     Page<AllocationResponse> response =allocationService.getAllAllocations(page,size,sortBy,direction);
     return ResponseEntity.ok(ApiResponse.<Page<AllocationResponse>>builder()
    	                .success(true)
    	                .message("Allocations fetched successfully")
    	                .data(response)
    	                .build());
 }
 
 @GetMapping("/product/{productId}")
 @Operation(summary = "Get Allocation By Product")
 public ResponseEntity<ApiResponse<Page<AllocationResponse>>> getAllocationByProduct(@PathVariable Long productId,@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {

     Page<AllocationResponse> response =allocationService.getAllocationByProduct(productId,page,size);
     return ResponseEntity.ok(ApiResponse.<Page<AllocationResponse>>builder()
    	                .success(true)
    	                .message("Allocations fetched successfully")
    	                .data(response)
    	                .build());
 }
 
 @GetMapping("/warehouse/{warehouseId}")
 @Operation(summary = "Get Allocation By Warehouse")
 public ResponseEntity<ApiResponse<Page<AllocationResponse>>> getAllocationByWarehouse( @PathVariable Long warehouseId, @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size) {

     Page<AllocationResponse> response =allocationService.getAllocationByWarehouse(warehouseId,page,size);
     return ResponseEntity.ok(ApiResponse.<Page<AllocationResponse>>builder()
    	                .success(true)
    	                .message("Allocations fetched successfully")
    	                .data(response)
    	                .build());
 }
 
 @GetMapping("/date-range")
 @Operation(summary = "Search Allocation By Date Range")
 public ResponseEntity<ApiResponse<Page<AllocationResponse>>> getAllocationByDateRange( @RequestParam LocalDateTime from,@RequestParam LocalDateTime to,
         @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

     Page<AllocationResponse> response =allocationService.getAllocationByDateRange(from,to,page,size);
     return ResponseEntity.ok(ApiResponse.<Page<AllocationResponse>>builder()
    	                .success(true)
    	                .message("Allocations fetched successfully")
    	                .data(response)
    	                .build());
 }
}