package com.hcmut.ecommerce.domain.feedback.dto.response;

import java.time.LocalDateTime;

import com.hcmut.ecommerce.domain.feedback.model.Feedback;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FeedbackResponse {
  private String buyerId;
  private Long productId;
  private String comment;
  private Integer rating;
  private LocalDateTime createdAt;

  public FeedbackResponse(Feedback feedback) {
    this.buyerId = feedback.getId().getBuyerId();
    this.productId = feedback.getId().getProductListingId().getProductId();
    this.comment = feedback.getComment();
    this.rating = feedback.getRating();
    this.createdAt = feedback.getCreatedAt();
  }

}
