package com.warehouse.serviceimpl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.warehouse.entity.Product;
import com.warehouse.exception.DuplicateResourceException;
import com.warehouse.exception.ProductNotFoundException;
import com.warehouse.mapper.ProductMapper;
import com.warehouse.repository.ProductRepository;
import com.warehouse.requestdto.ProductRequest;
import com.warehouse.responsedto.ProductResponse;
import com.warehouse.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

@Override
public ProductResponse createProduct(ProductRequest request) {
    log.info("Creating Product : {}", request.getName());
    validateDuplicateProduct(request);
    Product product = productMapper.toEntity(request);
    product = productRepository.save(product);
    log.info("Product Created Successfully : {}", product.getId());
    return productMapper.toResponse(product);

}

private void validateDuplicateProduct(ProductRequest request) {
    if (productRepository.existsBySkuAndDeletedFalse(request.getSku())) {
        throw new DuplicateResourceException("Product SKU already exists.");
    }

    if (productRepository.existsByNameIgnoreCaseAndDeletedFalse(request.getName())) {
        throw new DuplicateResourceException("Product name already exists.");
    }

}
@Override
public ProductResponse updateProduct(Long id,ProductRequest request) {
    log.info("Updating Product : {}", id);
    Product product = getProductEntity(id);

    validateProductForUpdate(product, request);

    product.setName(request.getName());
    product.setSku(request.getSku());
    product.setTotalStock(request.getTotalStock());

    product = productRepository.save(product);
    log.info("Product Updated Successfully : {}", id);
    return productMapper.toResponse(product);

}

	private void validateProductForUpdate(Product product,ProductRequest request) {
		productRepository.findBySkuAndDeletedFalse(request.getSku())
		.ifPresent(existing -> {
			if (!existing.getId().equals(product.getId())) {

throw new DuplicateResourceException(
"Product SKU already exists.");

}

});

		productRepository
		.findByNameIgnoreCaseAndDeletedFalse(request.getName())
		.ifPresent(existing -> {
			if (!existing.getId().equals(product.getId())) {
				throw new DuplicateResourceException("Product name already exists.");

}
});

}
    
	private Product getProductEntity(Long id) {
        return productRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id : " + id));

    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        log.info("Fetching Product with Id : {}", id);
        Product product = getProductEntity(id);
        return productMapper.toResponse(product);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(int page,int size,String sortBy,String direction) {
        log.info("Fetching Products | Page : {} | Size : {}", page, size);
        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository
                .findByDeletedFalse(pageable)
                .map(productMapper::toResponse);

    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Soft Deleting Product : {}", id);
        Product product = getProductEntity(id);
        product.setDeleted(true);
        product.setDeletedAt(LocalDateTime.now());
        productRepository.save(product);
        log.info("Product Soft Deleted Successfully : {}", id);

    }
}
           	