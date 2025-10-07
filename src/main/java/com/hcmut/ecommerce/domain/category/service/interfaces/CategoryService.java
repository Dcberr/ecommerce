package com.hcmut.ecommerce.domain.category.service.interfaces;

import com.hcmut.ecommerce.domain.category.dto.request.CreateCategoryRequest;
import com.hcmut.ecommerce.domain.category.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
  public CategoryResponse getCategoryById(Long id) throws Exception;

  public List<CategoryResponse> getAllCategories() throws Exception;

  public CategoryResponse createCategory(CreateCategoryRequest request) throws Exception;
}