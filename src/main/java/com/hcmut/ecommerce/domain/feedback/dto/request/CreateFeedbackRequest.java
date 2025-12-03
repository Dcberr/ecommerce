package com.hcmut.ecommerce.domain.feedback.dto.request;

import com.hcmut.ecommerce.domain.feedback.model.Feedback;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFeedbackRequest {
  private String comment;
  private Integer rating;

  public Feedback toFeedback() {
    return new Feedback(null, null, null, rating, comment, null);
  }
}
