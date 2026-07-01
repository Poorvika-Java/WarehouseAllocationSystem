package com.warehouse.controller;

import com.warehouse.requestdto.InventoryRequest;
import com.warehouse.responsedto.InventoryResponse;
import com.warehouse.service.WarehouseInventoryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventories")
@Tag(name = "Warehouse Inventory APIs", description = "Warehouse Inventory Management APIs")
@RequiredArgsConstructor
public class WarehouseInventoryController {

    private final WarehouseInventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(@RequestBody InventoryRequest request) {
        return new ResponseEntity<>(inventoryService.createInventory(request),HttpStatus.CREATED
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<InventoryResponse> updateInventory(@PathVariable Long id,@RequestBody InventoryRequest request) {
        return ResponseEntity.ok(inventoryService.updateInventory(id, request)
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id)
        );
    }


    @GetMapping
    public ResponseEntity<Page<InventoryResponse>> getAll(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,@RequestParam(defaultValue = "ASC") String direction) {

        return ResponseEntity.ok(inventoryService.getAllInventory(page, size, sortBy, direction)
        );
    }


    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<Page<InventoryResponse>> getByWarehouse(@PathVariable Long warehouseId,@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(inventoryService.getInventoryByWarehouse(warehouseId, page, size)
        );
    }


    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<InventoryResponse>> getByProduct(@PathVariable Long productId,@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(inventoryService.getInventoryByProduct(productId, page, size)
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }
}