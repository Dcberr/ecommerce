package com.hcmut.ecommerce.domain.productListing.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.hcmut.ecommerce.domain.product.model.Product;
import com.hcmut.ecommerce.domain.user.model.User;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "product_listings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductListing {
  @EmbeddedId
  private ProductListingId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_id", nullable = false)
  @MapsId("sellerId")
  private User seller;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  @MapsId("productId")
  private Product product;

  @NonNull
  private String name;

  private String description;

  @NonNull
  private Long stock;

  @NonNull
  private Float price;

  private String imageUrl;
  private String imageType;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @Embeddable
  @Data
  @NoArgsConstructor
  @RequiredArgsConstructor
  public static class ProductListingId implements Serializable {
    @NonNull
    private String sellerId;
    @NonNull
    private Long productId;
  }
}
