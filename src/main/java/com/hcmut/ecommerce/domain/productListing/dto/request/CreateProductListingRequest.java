package com.hcmut.ecommerce.domain.productListing.dto.request;

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
  private String imageUrl;

  public ProductListing toProductListing() {
    return new ProductListing(new ProductListingId(sellerId, productId), null, null, stock, price, imageUrl);
  }
}