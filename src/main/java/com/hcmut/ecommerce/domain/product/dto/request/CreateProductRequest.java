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
  private String imageUrl;
  @NonNull
  private List<Integer> categoryIds;

  public Product toProduct() {
    return new Product(null, name, baseUnit, imageUrl, null);
  }
}