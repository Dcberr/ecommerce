package com.hcmut.ecommerce.domain.productListing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmut.ecommerce.domain.productListing.model.ProductListing;

public interface ProductListingRepository extends JpaRepository<ProductListing, ProductListing.ProductListingId> {
}