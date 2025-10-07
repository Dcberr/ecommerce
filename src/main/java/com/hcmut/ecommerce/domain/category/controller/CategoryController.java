package com.hcmut.ecommerce.domain.category.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.hcmut.ecommerce.common.response.ApiResponse;
import com.hcmut.ecommerce.domain.category.dto.request.CreateCategoryRequest;
import com.hcmut.ecommerce.domain.category.dto.response.CategoryResponse;
import com.hcmut.ecommerce.domain.category.service.interfaces.CategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping("/{id}")
  public ApiResponse<CategoryResponse> getCategoryById(@PathVariable Long id) throws Exception {
    return ApiResponse.success(categoryService.getCategoryById(id), "Get Category Successfully!");
  }

  @GetMapping("")
  public ApiResponse<List<CategoryResponse>> getAllCategories() throws Exception {
    return ApiResponse.success(categoryService.getAllCategories(), "Get All Categories Successfully!");
  }

  @PostMapping("")
  public ApiResponse<CategoryResponse> createCategory(@RequestBody CreateCategoryRequest request) throws Exception {
    return ApiResponse.success(categoryService.createCategory(request), "Create Category Successfully!");
  }
}
