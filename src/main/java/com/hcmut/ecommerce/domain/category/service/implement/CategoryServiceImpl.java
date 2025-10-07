package com.hcmut.ecommerce.domain.category.service.implement;

import com.hcmut.ecommerce.domain.category.dto.request.CreateCategoryRequest;
import com.hcmut.ecommerce.domain.category.dto.response.CategoryResponse;
import com.hcmut.ecommerce.domain.category.model.Category;
import com.hcmut.ecommerce.domain.category.repository.CategoryRepository;
import com.hcmut.ecommerce.domain.category.service.interfaces.CategoryService;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  @Override
  public CategoryResponse getCategoryById(Long id) throws Exception {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    return new CategoryResponse(category);
  }

  @Override
  public List<CategoryResponse> getAllCategories() throws Exception {
    List<Category> categories = categoryRepository.findAll();
    return categories.stream().map(CategoryResponse::new).toList();
  }

  @Override
  public CategoryResponse createCategory(CreateCategoryRequest request) throws Exception {
    Category newCategory = categoryRepository.save(request.toCategory());
    return new CategoryResponse(newCategory);
  }
}