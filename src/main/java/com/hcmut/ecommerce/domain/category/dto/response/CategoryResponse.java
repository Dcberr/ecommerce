package com.hcmut.ecommerce.domain.category.dto.response;

import java.util.Set;

import com.hcmut.ecommerce.domain.category.model.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryResponse {
  private Long id;
  private String name;
  private String description;
  private Double price;
  private String imageUrl;
  private Set<String> categoryNames;

  public CategoryResponse(Category category) {
    this.id = category.getId();
    this.name = category.getName();
    this.description = category.getDescription();
  }
}
