package com.hcmut.ecommerce.domain.product.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.hcmut.ecommerce.common.response.ApiResponse;
import com.hcmut.ecommerce.domain.product.dto.request.CreateProductRequest;
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

  @GetMapping("")
  public ApiResponse<Page<ProductResponse>> getAllProducts(
      @RequestParam(required = false) Integer pageSize,
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false) String desc) throws Exception {
    if (pageSize == null)
      pageSize = 10;
    if (page == null)
      page = 0;
    if (sortBy == null)
      sortBy = "id";
    if (desc == null)
      desc = "false";
    Boolean isDesc = desc.equals("true") ? true : false;
    return ApiResponse.success(productService.getAllProducts(pageSize, page, sortBy, isDesc),
        "Get All Products Successfully!");
  }

  @PostMapping("")
  public ApiResponse<ProductResponse> createProdduct(@RequestBody CreateProductRequest request) throws Exception {
    return ApiResponse.success(productService.createProduct(request), "Create Products Successfully!");
  }

}
