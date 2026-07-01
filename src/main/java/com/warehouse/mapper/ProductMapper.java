package com.warehouse.mapper;

import org.springframework.stereotype.Component;

import com.warehouse.entity.Product;
import com.warehouse.requestdto.ProductRequest;
import com.warehouse.responsedto.ProductResponse;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .sku(request.getSku())
                .totalStock(request.getTotalStock())
                .deleted(false)
                .build();

    }

    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .totalStock(product.getTotalStock())
                .build();

    }

}