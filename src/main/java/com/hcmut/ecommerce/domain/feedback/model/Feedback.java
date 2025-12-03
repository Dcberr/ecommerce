package com.hcmut.ecommerce.domain.feedback.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.hcmut.ecommerce.domain.productListing.model.ProductListing;
import com.hcmut.ecommerce.domain.productListing.model.ProductListing.ProductListingId;
import com.hcmut.ecommerce.domain.user.model.User;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "feedback")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feedback {
  @EmbeddedId
  private FeedbackId id;

  @MapsId("buyerId")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "buyer_id", referencedColumnName = "id", nullable = false)
  private User buyer;

  @MapsId("productListingId")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "seller_id", referencedColumnName = "seller_id", nullable = false),
      @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
  })
  private ProductListing productListing;

  @NonNull
  private Integer rating;

  private String comment;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @Embeddable
  @Data
  @NoArgsConstructor
  @RequiredArgsConstructor
  public static class FeedbackId implements Serializable {
    @NonNull
    private String buyerId;

    @NonNull
    private ProductListingId productListingId;
  }

}
