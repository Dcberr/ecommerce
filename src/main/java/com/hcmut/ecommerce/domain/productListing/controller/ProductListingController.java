package com.hcmut.ecommerce.domain.productListing.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/productListings")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "ProductListings", description = "Product listing operations")
public class ProductListingController {

  private final ProductListingService productListingService;

  @Operation(
    summary = "List product listings (paged)",
    description = "Retrieve paged list of product listings. Supports page, pageSize, sorting and descending flag.",
    tags = {"ProductListings"}
  )
  @io.swagger.v3.oas.annotations.responses.ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Paged product listings returned"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @GetMapping("")
  public ApiResponse<Page<ProductListingResponse>> getAllProductListings(
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
    return ApiResponse.success(productListingService.getAllProductListings(pageSize, page, sortBy, isDesc),
        "Get All ProductListings Successfully!");
  }

  @Operation(
    summary = "Create product listing",
    description = "Create a new product listing. Requires appropriate role/permission.",
    tags = {"ProductListings"}
  )
  @io.swagger.v3.oas.annotations.responses.ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product listing created"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @PostMapping("")
  public ApiResponse<ProductListingResponse> createProductListing(@RequestBody CreateProductListingRequest request)
      throws Exception {
    return ApiResponse.success(productListingService.createProductListing(request),
        "Create ProductListings Successfully!");
  }

}
