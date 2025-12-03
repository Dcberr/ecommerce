package com.hcmut.ecommerce.domain.feedback.service.interfaces;

import org.springframework.data.domain.Page;

import com.hcmut.ecommerce.domain.feedback.dto.response.FeedbackResponse;

public interface FeedbackService {

  public FeedbackResponse createFeedback(String buyerId, String sellerId, Long productId, String comment,
      Integer rating);

  public FeedbackResponse getFeedbackById(String buyerId, String sellerId, Long productId);

  public Page<FeedbackResponse> getAllFeedbacks(Integer pageSize, Integer page, String sortBy, Boolean desc,
      String sellerId, Long productId)
      throws Exception;

}
