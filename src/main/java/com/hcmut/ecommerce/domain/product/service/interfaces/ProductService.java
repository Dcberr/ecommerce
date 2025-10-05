package com.hcmut.ecommerce.domain.product.service.interfaces;

import com.hcmut.ecommerce.domain.product.dto.request.CreateProductRequest;
import com.hcmut.ecommerce.domain.product.dto.response.ProductResponse;

import org.springframework.data.domain.Page;

public interface ProductService {
  public ProductResponse getProductById(Long id) throws Exception;

  public Page<ProductResponse> getAllProducts(Integer pageSize, Integer page, String sortBy, Boolean desc)
      throws Exception;

  public ProductResponse createProduct(CreateProductRequest request) throws Exception;
}