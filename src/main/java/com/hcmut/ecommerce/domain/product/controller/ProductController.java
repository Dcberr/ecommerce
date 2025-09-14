package com.hcmut.ecommerce.domain.product.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmut.ecommerce.common.response.ApiResponse;
import com.hcmut.ecommerce.domain.product.dto.response.ProductResponse;
import com.hcmut.ecommerce.domain.product.service.interfaces.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

  private final ProductService productService;

  @GetMapping("/{id}")
  public ApiResponse<ProductResponse> getProductById(@PathVariable Long id) throws Exception {
    return ApiResponse.success(productService.getProductById(id), "Get Product Successfully!");
  }

}
