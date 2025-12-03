package com.hcmut.ecommerce.domain.productListing.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.hcmut.ecommerce.common.response.ApiResponse;
import com.hcmut.ecommerce.domain.productListing.dto.request.CreateProductListingRequest;
import com.hcmut.ecommerce.domain.productListing.dto.response.ProductListingResponse;
import com.hcmut.ecommerce.domain.productListing.service.interfaces.ProductListingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/productListings")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "ProductListings", description = "Product listing operations")
public class ProductListingController {

  private final ProductListingService productListingService;

  @Operation(summary = "List product listings (paged)", description = "Retrieve paged list of product listings. Supports page, pageSize, sorting and descending flag.", tags = {
      "ProductListings" })
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Paged product listings returned"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @GetMapping("")
  public ApiResponse<Page<ProductListingResponse>> getAllProductListings(
      @Parameter(description = "Number of items per page", example = "10") @RequestParam(required = true) Integer pageSize,
      @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(required = true) Integer page,
      @Parameter(description = "Field to sort by", example = "id") @RequestParam(required = true) String sortBy,
      @Parameter(description = "Set true to sort descending", example = "false") @RequestParam(required = false) String desc,
      @Parameter(description = "Keyword to search in product name and description", example = "Chicken") @RequestParam(required = false) String keyword,
      @Parameter(description = "Minimum price filter", example = "10000") @RequestParam(required = false) Integer minPrice,
      @Parameter(description = "Maximum price filter", example = "500000") @RequestParam(required = false) Integer maxPrice,
      @Parameter(description = "Category name filter", example = "Meat") @RequestParam(required = false) String categoryName,
      @Parameter(description = "Province filter", example = "Ha Noi") @RequestParam(required = false) String province)
      throws Exception {
    if (pageSize == null)
      pageSize = 10;
    if (page == null)
      page = 0;
    if (sortBy == null)
      sortBy = "id";
    if (desc == null)
      desc = "false";
    Boolean isDesc = desc.equals("true") ? true : false;
    return ApiResponse.success(
        productListingService.getAllProductListings(pageSize, page, sortBy, isDesc, keyword, minPrice, maxPrice,
            categoryName, province),
        "Get All ProductListings Successfully!");
  }

  @Operation(summary = "Get product listing by id", description = "Retrieve a single product listing by its seller ID and product ID", tags = {
      "ProductListings" })
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product listing returned"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product listing not found"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @GetMapping("/{sellerId}/{productId}")
  public ApiResponse<ProductListingResponse> getProductListingById(
      @Parameter(description = "Seller ID", example = "seller123") @PathVariable String sellerId,
      @Parameter(description = "Product ID", example = "1") @PathVariable Long productId)
      throws Exception {
    return ApiResponse.success(productListingService.getProductListingById(sellerId, productId),
        "Get ProductListing By ID Successfully!");
  }

  @Operation(summary = "Create product listing", description = "Create a new product listing. Requires appropriate role/permission.", tags = {
      "ProductListings" })
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product listing created"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @SecurityRequirement(name = "BearerAuth")
  @PostMapping("")
  public ApiResponse<ProductListingResponse> createProductListing(@RequestBody CreateProductListingRequest request)
      throws Exception {
    return ApiResponse.success(productListingService.createProductListing(request),
        "Create ProductListings Successfully!");
  }

}
