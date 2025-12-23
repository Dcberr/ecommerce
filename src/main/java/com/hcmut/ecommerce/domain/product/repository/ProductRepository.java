package com.hcmut.ecommerce.domain.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmut.ecommerce.domain.product.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findBySellerId(String sellerId, Pageable pageable);
}