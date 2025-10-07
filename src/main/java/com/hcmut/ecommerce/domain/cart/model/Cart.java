package com.hcmut.ecommerce.domain.cart.model;

import java.io.Serializable;

import com.hcmut.ecommerce.domain.productListing.model.ProductListing;
import com.hcmut.ecommerce.domain.productListing.model.ProductListing.ProductListingId;
import com.hcmut.ecommerce.domain.user.model.User;

import io.micrometer.common.lang.NonNull;
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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

  @EmbeddedId
  private CartId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "buyer_id", nullable = false)
  @MapsId("buyerId")
  private User buyer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "product_id", nullable = false),
      @JoinColumn(name = "seller_id", nullable = false)
  })
  @MapsId("listingId")
  private ProductListing listing;

  @NonNull
  private Long amount;

  @Embeddable
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CartId implements Serializable {
    @NonNull
    private String buyerId;
    @NonNull
    private ProductListingId listingId;
  }

}
