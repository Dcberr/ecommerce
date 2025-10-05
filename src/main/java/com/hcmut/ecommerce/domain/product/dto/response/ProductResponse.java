package com.hcmut.ecommerce.domain.product.dto.response;

import java.util.stream.Collectors;
import java.util.List;

import com.hcmut.ecommerce.domain.product.model.Product;
import com.hcmut.ecommerce.domain.product.model.ProductUnitType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponse {
  private Long id;
  private String name;
  private ProductUnitType baseUnit;
  private String imageUrl;
  private List<String> categoryNames;

  public ProductResponse(Product product) {
    this.id = product.getId();
    this.name = product.getName();
    this.baseUnit = product.getBaseUnit();
    this.imageUrl = product.getImageUrl();
    this.categoryNames = product.getCategories().stream().map(category -> category.getName())
        .collect(Collectors.toList());
  }
}
