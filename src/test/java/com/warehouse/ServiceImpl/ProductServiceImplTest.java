package com.warehouse.ServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.warehouse.entity.Product;
import com.warehouse.exception.DuplicateResourceException;
import com.warehouse.exception.ProductNotFoundException;
import com.warehouse.mapper.ProductMapper;
import com.warehouse.repository.ProductRepository;
import com.warehouse.requestdto.ProductRequest;
import com.warehouse.responsedto.ProductResponse;
import com.warehouse.serviceimpl.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductRequest request;
    private ProductResponse response;

    @BeforeEach
    void setUp() {

        request = new ProductRequest();
        request.setName("Laptop");
        request.setSku("LAP100");
        request.setTotalStock(50);

        product = Product.builder()
                .id(1L)
                .name("Laptop")
                .sku("LAP100")
                .totalStock(50)
                .deleted(false)
                .build();

        response = ProductResponse.builder()
                .id(1L)
                .name("Laptop")
                .sku("LAP100")
                .totalStock(50)
                .build();
    }
    
    @Test
    void testCreateProduct_Success() {

        when(productRepository.existsBySkuAndDeletedFalse("LAP100"))
                .thenReturn(false);

        when(productRepository.existsByNameIgnoreCaseAndDeletedFalse("Laptop"))
                .thenReturn(false);

        when(productMapper.toEntity(request))
                .thenReturn(product);

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        when(productMapper.toResponse(product))
                .thenReturn(response);

        ProductResponse result = productService.createProduct(request);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        assertEquals("LAP100", result.getSku());

        verify(productRepository).save(product);
        verify(productMapper).toEntity(request);
        verify(productMapper).toResponse(product);
    }
    
    @Test
    void testCreateProduct_DuplicateSku() {
        when(productRepository.existsBySkuAndDeletedFalse("LAP100"))
                .thenReturn(true);
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> productService.createProduct(request));

        assertEquals("Product SKU already exists.", exception.getMessage());

        verify(productRepository, never()).save(any(Product.class));
        verify(productMapper, never()).toEntity(any(ProductRequest.class));
    }
    
    @Test
    void testCreateProduct_DuplicateName() {
        when(productRepository.existsBySkuAndDeletedFalse("LAP100"))
                .thenReturn(false);
        when(productRepository.existsByNameIgnoreCaseAndDeletedFalse("Laptop"))
                .thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> productService.createProduct(request));

        assertEquals("Product name already exists.", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
        verify(productMapper, never()).toEntity(any(ProductRequest.class));
    }
    
    @Test
    void testGetProductById_Success() {
        when(productRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(product));
        when(productMapper.toResponse(product))
                .thenReturn(response);
        ProductResponse result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());

        verify(productRepository).findByIdAndDeletedFalse(1L);
        verify(productMapper).toResponse(product);
    }
    
    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productService.getProductById(1L));
        verify(productMapper, never()).toResponse(any(Product.class));
    }
}