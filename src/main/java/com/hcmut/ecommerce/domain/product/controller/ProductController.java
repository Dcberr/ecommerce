package com.hcmut.ecommerce.domain.product.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.hcmut.ecommerce.common.response.ApiResponse;
import com.hcmut.ecommerce.domain.product.dto.request.CreateProductRequest;
import com.hcmut.ecommerce.domain.product.dto.response.ProductResponse;
import com.hcmut.ecommerce.domain.product.service.interfaces.ProductService;
import com.hcmut.ecommerce.domain.user.model.User;
import com.hcmut.ecommerce.domain.user.service.interfaces.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Products", description = "Product CRUD and search")
public class ProductController {
  
  private final ProductService productService;

  @Operation(summary = "Get product by id", description = "Retrieve a single product by its id", tags = {"Products"})
  @io.swagger.v3.oas.annotations.responses.ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product returned"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @GetMapping("/{id}")
  public ApiResponse<ProductResponse> getProductById(@PathVariable Long id) throws Exception {
    return ApiResponse.success(productService.getProductById(id), "Get Product Successfully!");
  }

  @Operation(
    summary = "List products (paged)",
    description = "Retrieve paged list of products. Supports page, pageSize, sorting and descending flag.",
    tags = {"Products"}
  )
  @io.swagger.v3.oas.annotations.responses.ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Paged products returned"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @GetMapping("")
  public ApiResponse<Page<ProductResponse>> getAllProducts(
      @Parameter(description = "Number of items per page", example = "10") @RequestParam(required = false) Integer pageSize,
      @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(required = false) Integer page,
      @Parameter(description = "Field to sort by", example = "id") @RequestParam(required = false) String sortBy,
      @Parameter(description = "Set true to sort descending", example = "false") @RequestParam(required = false) String desc) throws Exception {
    if (pageSize == null)
      pageSize = 10;
    if (page == null)
      page = 0;
    if (sortBy == null)
      sortBy = "id";
    if (desc == null)
      desc = "false";
    Boolean isDesc = desc.equals("true") ? true : false;
    return ApiResponse.success(productService.getAllProducts(pageSize, page, sortBy, isDesc),
        "Get All Products Successfully!");
  }

  @Operation(summary = "Create product", description = "Create a new product. Requires appropriate role/permission.", tags = {"Products"})
  @io.swagger.v3.oas.annotations.responses.ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product created"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @PostMapping("")
  public ApiResponse<ProductResponse> createProduct(@RequestBody CreateProductRequest request) throws Exception {
    return ApiResponse.success(productService.createProduct(request), "Create Products Successfully!");
  }

  @Operation(
      summary = "List products by seller (paged)",
      description = "Retrieve paged list of products by seller id. Supports page, pageSize, sorting and descending flag.",
      tags = {"Products"}
  )
  @io.swagger.v3.oas.annotations.responses.ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Paged products returned"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @GetMapping("/seller")
  public ApiResponse<Page<ProductResponse>> getProductBySellerId(
      @Parameter(description = "Number of items per page", example = "10") @RequestParam(required = false) Integer pageSize,
      @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(required = false) Integer page,
      @Parameter(description = "Field to sort by", example = "id") @RequestParam(required = false) String sortBy,
      @Parameter(description = "Set true to sort descending", example = "false") @RequestParam(required = false) String desc) throws Exception {
    if (pageSize == null)
      pageSize = 10;
    if (page == null)
      page = 0;
    if (sortBy == null)
      sortBy = "id";
    if (desc == null)
      desc = "false";

    Boolean isDesc = desc.equals("true") ? true : false;
    return ApiResponse.success(productService.getProductBySellerId(pageSize, page, sortBy, isDesc),
        "Get Products By Seller Successfully!");
  }

}
