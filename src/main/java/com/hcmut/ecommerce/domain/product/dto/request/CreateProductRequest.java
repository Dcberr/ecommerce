package com.hcmut.ecommerce.domain.product.dto.request;

import java.util.List;

import com.hcmut.ecommerce.domain.product.model.Product;
import com.hcmut.ecommerce.domain.product.model.Product.ProductUnitType;

import io.micrometer.common.lang.NonNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequest {
  @NonNull
  private String name;
  @NonNull
  private ProductUnitType baseUnit;
  private Long price;
  private int discount;
  private Float rating;
  private String location;
  @NonNull
  private List<Integer> categoryIds;

  private String imageBlobString;
  private String imageType;

  public Product toProduct() {
    return new Product(null, name, baseUnit, null, imageType, price, discount, rating, location, null);
  }
}