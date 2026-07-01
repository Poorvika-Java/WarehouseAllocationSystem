package com.warehouse.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.warehouse.requestdto.WarehouseRequest;
import com.warehouse.responsedto.ApiResponse;
import com.warehouse.responsedto.WarehouseResponse;
import com.warehouse.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
@Tag(name = "Warehouse APIs", description = "Warehouse Management APIs")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Warehouse")
    public ApiResponse<WarehouseResponse> createWarehouse(@Valid @RequestBody WarehouseRequest request) {
        return ApiResponse.<WarehouseResponse>builder()
                .success(true)
                .message("Warehouse Created Successfully")
                .data(warehouseService.createWarehouse(request))
                .build();

    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Warehouse By Id")
    public ApiResponse<WarehouseResponse> getWarehouseById(@PathVariable Long id) {
        return ApiResponse.<WarehouseResponse>builder()
                .success(true)
                .message("Warehouse Details")
                .data(warehouseService.getWarehouseById(id))
                .build();

    }

    @GetMapping
    public Page<WarehouseResponse> getAll(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,@RequestParam(defaultValue = "asc") String direction) {
        return warehouseService.getAllWarehouses(page, size, sortBy, direction);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Warehouse")
    public ApiResponse<WarehouseResponse> updateWarehouse(@PathVariable Long id,@Valid @RequestBody WarehouseRequest request) {
        return ApiResponse.<WarehouseResponse>builder()
                .success(true)
                .message("Warehouse Updated Successfully")
                .data(warehouseService.updateWarehouse(id, request))
                .build();

    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate Warehouse")
    public ApiResponse<WarehouseResponse> activateWarehouse(@PathVariable Long id) {
        return ApiResponse.<WarehouseResponse>builder()
                .success(true)
                .message("Warehouse Activated Successfully")
                .data(warehouseService.activateWarehouse(id))
                .build();

    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate Warehouse")
    public ApiResponse<WarehouseResponse> deactivateWarehouse( @PathVariable Long id) {
        return ApiResponse.<WarehouseResponse>builder()
                .success(true)
                .message("Warehouse Deactivated Successfully")
                .data(warehouseService.deactivateWarehouse(id))
                .build();

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft Delete Warehouse")
    public ApiResponse<String> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ApiResponse.<String>builder()
                .success(true)
                .message("Warehouse Deleted Successfully")
                .data("SUCCESS")
                .build();

    }

}