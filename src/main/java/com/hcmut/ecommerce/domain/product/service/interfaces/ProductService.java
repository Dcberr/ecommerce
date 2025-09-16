package com.hcmut.ecommerce.domain.product.service.interfaces;

import com.hcmut.ecommerce.domain.product.dto.response.ProductResponse;

public interface ProductService {
  public ProductResponse getProductById(Long id) throws Exception;
}