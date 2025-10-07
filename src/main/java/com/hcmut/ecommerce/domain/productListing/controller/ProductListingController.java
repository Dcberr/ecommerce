package com.hcmut.ecommerce.domain.productListing.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.hcmut.ecommerce.common.response.ApiResponse;
import com.hcmut.ecommerce.domain.productListing.dto.request.CreateProductListingRequest;
import com.hcmut.ecommerce.domain.productListing.dto.response.ProductListingResponse;
import com.hcmut.ecommerce.domain.productListing.service.interfaces.ProductListingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/productListings")
@RequiredArgsConstructor
@Slf4j
public class ProductListingController {

  private final ProductListingService productListingService;

  // @GetMapping("/{id}")
  // public ApiResponse<ProductListingResponse>
  // getProductListingById(@PathVariable Long id) throws Exception {
  // return ApiResponse.success(productListingService.getProductListingById(id),
  // "Get ProductListing Successfully!");
  // }

  @GetMapping("")
  public ApiResponse<Page<ProductListingResponse>> getAllProductListings(
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
    return ApiResponse.success(productListingService.getAllProductListings(pageSize, page, sortBy, isDesc),
        "Get All ProductListings Successfully!");
  }

  @PostMapping("")
  public ApiResponse<ProductListingResponse> createProductListing(@RequestBody CreateProductListingRequest request)
      throws Exception {
    return ApiResponse.success(productListingService.createProductListing(request),
        "Create ProductListings Successfully!");
  }

}
