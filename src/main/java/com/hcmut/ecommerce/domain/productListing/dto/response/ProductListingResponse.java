package com.hcmut.ecommerce.domain.productListing.dto.response;

import com.hcmut.ecommerce.domain.productListing.model.ProductListing;

import lombok.Data;

@Data
public class ProductListingResponse {
  private String sellerId;
  private Long productId;
  private Long stock;
  private Float price;
  private String imageUrl;
  private String productName;

  public ProductListingResponse(ProductListing productListing) {
    this.sellerId = productListing.getSeller().getId();
    this.productId = productListing.getProduct().getId();
    this.stock = productListing.getStock();
    this.price = productListing.getPrice();
    this.imageUrl = productListing.getImageUrl();
    this.productName = productListing.getProduct().getName();
  }
}
