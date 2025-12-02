package com.hcmut.ecommerce.domain.feedback.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hcmut.ecommerce.domain.feedback.dto.request.CreateFeedbackRequest;
import com.hcmut.ecommerce.domain.feedback.dto.response.FeedbackResponse;
import com.hcmut.ecommerce.domain.feedback.service.interfaces.FeedbackService;
import com.hcmut.ecommerce.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/feedbacks")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Feedbacks", description = "Feedback operations")
public class FeedbackController {

  private final FeedbackService feedbackService;

  @Operation(summary = "List feedbacks (paged)", description = "Retrieve paged list of feedbacks for a specific product listing. Supports page, pageSize, sorting and descending flag.", tags = {
      "Feedbacks" })
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Paged feedbacks returned"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @GetMapping("/{sellerId}/{productId}")
  public ApiResponse<Page<FeedbackResponse>> getAllFeedbacks(
      @Parameter(description = "Page size", example = "10") @RequestParam Integer pageSize,
      @Parameter(description = "Page number", example = "0") @RequestParam Integer page,
      @Parameter(description = "Sort by field", example = "createdAt") @RequestParam String sortBy,
      @Parameter(description = "Descending order", example = "true") @RequestParam Boolean desc,
      @Parameter(description = "Seller ID") @PathVariable String sellerId,
      @Parameter(description = "Product ID", example = "1") @PathVariable Long productId) throws Exception {
    Page<FeedbackResponse> response = feedbackService.getAllFeedbacks(pageSize, page, sortBy, desc,
        sellerId,
        productId);
    return ApiResponse.success(response);
  }

  @Operation(summary = "Get feedback by ID", description = "Retrieve a specific feedback by buyer ID, seller ID, and product ID.", tags = {
      "Feedbacks" })
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Feedback returned"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @GetMapping("/{sellerId}/{productId}/{buyerId}")
  public ApiResponse<FeedbackResponse> getFeedbackById(
      @Parameter(description = "Buyer ID") @PathVariable String buyerId,
      @Parameter(description = "Seller ID") @PathVariable String sellerId,
      @Parameter(description = "Product ID", example = "1") @PathVariable Long productId) {
    FeedbackResponse response = feedbackService.getFeedbackById(buyerId, sellerId, productId);
    return ApiResponse.success(response);

  }

  @Operation(summary = "Create feedback", description = "Create a new feedback for a specific product listing by a buyer.", tags = {
      "Feedbacks" })
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Feedback created successfully"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @PostMapping("/{sellerId}/{productId}/{buyerId}")
  public ApiResponse<FeedbackResponse> createFeedback(
      @Parameter(description = "Seller ID") @PathVariable String sellerId,
      @Parameter(description = "Product ID", example = "1") @PathVariable Long productId,
      @RequestBody CreateFeedbackRequest request,
      @Parameter(description = "Buyer ID") @PathVariable String buyerId) {
    FeedbackResponse response = feedbackService.createFeedback(buyerId, sellerId, productId,
        request.getComment(),
        request.getRating());
    return ApiResponse.success(response);
  }

}
