package com.hcmut.ecommerce.domain.feedback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcmut.ecommerce.domain.feedback.model.Feedback;
import com.hcmut.ecommerce.domain.productListing.model.ProductListing.ProductListingId;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Feedback.FeedbackId> {
  Page<Feedback> findByIdProductListingId(ProductListingId productListingId, Pageable pageable);
}
