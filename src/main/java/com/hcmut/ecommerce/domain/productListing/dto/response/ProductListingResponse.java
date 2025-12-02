package com.hcmut.ecommerce.domain.productListing.dto.response;

import java.time.LocalDateTime;

import com.hcmut.ecommerce.domain.productListing.model.ProductListing;

import lombok.Data;

@Data
public class ProductListingResponse {
  private String sellerId;
  private Long productId;
  private Long stock;
  private Float price;
  private String name;
  private String description;
  private String imageUrl;
  private String imageType;
  private String productName;
  private String productUnit;
  private String location;
  private LocalDateTime createdAt;

  public ProductListingResponse(ProductListing productListing) {
    this.sellerId = productListing.getSeller().getId();
    this.productId = productListing.getProduct().getId();
    this.stock = productListing.getStock();
    this.price = productListing.getPrice();
    this.imageUrl = productListing.getImageUrl();
    this.imageType = productListing.getImageType();
    this.name = productListing.getName();
    this.description = productListing.getDescription();
    this.productName = productListing.getProduct().getName();
    this.productUnit = productListing.getProduct().getBaseUnit().toString();
    this.location = productListing.getSeller().getAddress() + ", " + productListing.getSeller().getWard() + ", "
        + productListing.getSeller().getProvince();
    this.createdAt = productListing.getCreatedAt();
  }
}
