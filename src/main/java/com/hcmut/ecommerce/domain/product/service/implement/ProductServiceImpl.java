package com.hcmut.ecommerce.domain.product.service.implement;

import com.hcmut.ecommerce.domain.product.dto.response.ProductResponse;
import com.hcmut.ecommerce.domain.product.model.Product;
import com.hcmut.ecommerce.domain.product.repository.ProductRepository;
import com.hcmut.ecommerce.domain.product.service.interfaces.ProductService;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  @Override
  public ProductResponse getProductById(Long id) throws Exception {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    return new ProductResponse(product);
  }

}