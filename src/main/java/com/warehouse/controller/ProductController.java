package com.warehouse.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.warehouse.requestdto.ProductRequest;
import com.warehouse.responsedto.ApiResponse;
import com.warehouse.responsedto.ProductResponse;
import com.warehouse.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product APIs", description = "Product Management APIs")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Product")
    public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Product Created Successfully")
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Product By Id")
    public ApiResponse<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse response = productService.getProductById(id);
        return ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Product Details")
                .data(response)
                .build();
    }

    @GetMapping
    @Operation(summary = "Get All Products")
    public ApiResponse<Page<ProductResponse>> getAllProducts(@RequestParam(defaultValue = "0")int page,@RequestParam(defaultValue = "10")int size,
            @RequestParam(defaultValue = "id")String sortBy,@RequestParam(defaultValue = "ASC")String direction) {
        Page<ProductResponse> response =productService.getAllProducts(page,size,sortBy,direction);
        return ApiResponse.<Page<ProductResponse>>builder()
                .success(true)
                .message("Product List")
                .data(response)
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Product")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable Long id,@Valid @RequestBody ProductRequest request) {
        ProductResponse response =productService.updateProduct(id, request);
        return ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Product Updated Successfully")
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft Delete Product")
    public ApiResponse<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ApiResponse.<String>builder()
                .success(true)
                .message("Product Deleted Successfully")
                .data("SUCCESS")
                .build();
    }

}