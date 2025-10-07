package com.hcmut.ecommerce.domain.productListing.service.interfaces;

import com.hcmut.ecommerce.domain.productListing.dto.request.CreateProductListingRequest;
import com.hcmut.ecommerce.domain.productListing.dto.response.ProductListingResponse;

import org.springframework.data.domain.Page;

public interface ProductListingService {
  // public ProductListingResponse getProductListingById(Long id) throws
  // Exception;

  public Page<ProductListingResponse> getAllProductListings(Integer pageSize, Integer page, String sortBy, Boolean desc)
      throws Exception;

  public ProductListingResponse createProductListing(CreateProductListingRequest request) throws Exception;
}