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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Categories", description = "Category management")
public class CategoryController {

  private final CategoryService categoryService;

  @Operation(summary = "Get category by id", description = "Retrieve a single category by its id", tags = {"Categories"})
  @io.swagger.v3.oas.annotations.responses.ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category returned"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @GetMapping("/{id}")
  public ApiResponse<CategoryResponse> getCategoryById(@PathVariable Long id) throws Exception {
    return ApiResponse.success(categoryService.getCategoryById(id), "Get Category Successfully!");
  }

  @Operation(summary = "List all categories", description = "Retrieve a list of all categories", tags = {"Categories"})
  @io.swagger.v3.oas.annotations.responses.ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categories returned"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @GetMapping("")
  public ApiResponse<List<CategoryResponse>> getAllCategories() throws Exception {
    return ApiResponse.success(categoryService.getAllCategories(), "Get All Categories Successfully!");
  }

  @Operation(summary = "Create category", description = "Create a new category. Requires appropriate role/permission.", tags = {"Categories"})
  @io.swagger.v3.oas.annotations.responses.ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category created"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @PostMapping("")
  public ApiResponse<CategoryResponse> createCategory(@RequestBody CreateCategoryRequest request) throws Exception {
    return ApiResponse.success(categoryService.createCategory(request), "Create Category Successfully!");
  }
}
