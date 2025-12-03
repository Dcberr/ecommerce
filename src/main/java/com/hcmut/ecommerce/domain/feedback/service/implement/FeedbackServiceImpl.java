package com.hcmut.ecommerce.domain.feedback.service.implement;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hcmut.ecommerce.domain.feedback.dto.response.FeedbackResponse;
import com.hcmut.ecommerce.domain.feedback.model.Feedback;
import com.hcmut.ecommerce.domain.feedback.model.Feedback.FeedbackId;
import com.hcmut.ecommerce.domain.feedback.repository.FeedbackRepository;
import com.hcmut.ecommerce.domain.feedback.service.interfaces.FeedbackService;
import com.hcmut.ecommerce.domain.productListing.model.ProductListing;
import com.hcmut.ecommerce.domain.productListing.model.ProductListing.ProductListingId;
import com.hcmut.ecommerce.domain.productListing.repository.ProductListingRepository;
import com.hcmut.ecommerce.domain.user.model.User;
import com.hcmut.ecommerce.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

  private final FeedbackRepository feedbackRepository;
  private final UserRepository userRepository;
  private final ProductListingRepository productListingRepository;

  public FeedbackResponse createFeedback(String buyerId, String sellerId, Long productId, String comment,
      Integer rating) {
    ProductListingId productListingId = new ProductListingId(sellerId, productId);
    FeedbackId feedbackId = new FeedbackId(buyerId, productListingId);
    User buyer = userRepository.findById(buyerId)
        .orElseThrow(() -> new RuntimeException("Buyer not found"));
    ProductListing productListing = productListingRepository.findById(productListingId)
        .orElseThrow(() -> new RuntimeException("ProductListing not found"));
    Feedback feedback = new Feedback(feedbackId, buyer, productListing, rating, comment, LocalDateTime.now());
    Feedback savedFeedback = feedbackRepository.save(feedback);
    return new FeedbackResponse(savedFeedback);
  }

  public FeedbackResponse getFeedbackById(String buyerId, String sellerId, Long productId) {
    ProductListingId productListingId = new ProductListingId(sellerId, productId);
    FeedbackId feedbackId = new FeedbackId(buyerId, productListingId);
    Feedback feedback = feedbackRepository.findById(feedbackId)
        .orElseThrow(() -> new RuntimeException("Feedback not found"));
    return new FeedbackResponse(feedback);
  }

  public Page<FeedbackResponse> getAllFeedbacks(Integer pageSize, Integer page, String sortBy, Boolean desc,
      String sellerId, Long productId)
      throws Exception {
    Pageable pageable = PageRequest.of(page, pageSize,
        desc ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending());
    ProductListingId productListingId = new ProductListingId(sellerId, productId);
    Page<Feedback> feedbacks = feedbackRepository.findByIdProductListingId(productListingId, pageable);
    return feedbacks.map(FeedbackResponse::new);
  }

}
