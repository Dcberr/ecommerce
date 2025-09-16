package com.hcmut.ecommerce.domain.product.dto.response;

import java.util.Set;

import com.hcmut.ecommerce.domain.product.model.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponse {
  private Long id;
  private String name;
  private String description;
  private Double price;
  private String imageUrl;
  private Set<String> categoryNames;

  public ProductResponse(Product product) {
    this.id = product.getId();
    this.name = product.getName();
    this.description = product.getDescription();
    this.price = product.getPrice();
    this.imageUrl = product.getImageUrl();

    this.categoryNames = product.getCategories().stream().map(category -> category.getName())
        .collect(java.util.stream.Collectors.toSet());
  }
}
