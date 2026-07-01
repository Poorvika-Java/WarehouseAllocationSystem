package com.warehouse.service;

import org.springframework.data.domain.Page;

import com.warehouse.requestdto.ProductRequest;
import com.warehouse.responsedto.ProductResponse;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(Long id,ProductRequest request);

    ProductResponse getProductById(Long id);

    Page<ProductResponse> getAllProducts(int page,int size,String sortBy,String direction);

    void deleteProduct(Long id);

}