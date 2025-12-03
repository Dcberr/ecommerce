package com.hcmut.ecommerce.domain.category.dto.request;

import com.hcmut.ecommerce.domain.category.model.Category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequest {
  private String name;
  private String description;
  private String imageBlobString;
  private String imageType;

  public Category toCategory() {
    return new Category(null, name, description, null, imageType);
  }
}