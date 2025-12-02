package com.hcmut.ecommerce.domain.productListing.dto.request;

import java.time.LocalDateTime;

import com.hcmut.ecommerce.domain.productListing.model.ProductListing;
import com.hcmut.ecommerce.domain.productListing.model.ProductListing.ProductListingId;

import io.micrometer.common.lang.NonNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductListingRequest {
  @NonNull
  private String sellerId;
  @NonNull
  private Long productId;
  @NonNull
  private Long stock;
  @NonNull
  private Float price;
  @NonNull
  private String name;
  private String description;

  private String imageBlobString;
  private String imageType;

  public ProductListing toProductListing() {
    ProductListing productListing = new ProductListing();
    ProductListingId id = new ProductListingId(sellerId, productId);
    productListing.setId(id);
    productListing.setStock(stock);
    productListing.setPrice(price);
    productListing.setName(name);
    productListing.setDescription(description);
    productListing.setImageType(imageType);
    productListing.setCreatedAt(LocalDateTime.now());
    return productListing;
  }
}