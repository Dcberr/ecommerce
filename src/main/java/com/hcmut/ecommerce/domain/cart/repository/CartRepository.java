package com.hcmut.ecommerce.domain.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmut.ecommerce.domain.cart.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Cart.CartId> {
  void deleteAllById_BuyerId(String buyerId);
}