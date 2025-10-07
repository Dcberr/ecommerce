package com.hcmut.ecommerce.domain.user.service.implement;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.hcmut.ecommerce.domain.cart.dto.request.CreateCartRequest;
import com.hcmut.ecommerce.domain.cart.dto.request.DeleteCartRequest;
import com.hcmut.ecommerce.domain.cart.dto.response.CartResponse;
import com.hcmut.ecommerce.domain.cart.model.Cart;
import com.hcmut.ecommerce.domain.cart.repository.CartRepository;
import com.hcmut.ecommerce.domain.productListing.model.ProductListing;
import com.hcmut.ecommerce.domain.productListing.repository.ProductListingRepository;
import com.hcmut.ecommerce.domain.user.model.User;
import com.hcmut.ecommerce.domain.user.repository.UserRepository;
import com.hcmut.ecommerce.domain.user.service.interfaces.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final CartRepository cartRepository;
  private final ProductListingRepository listingRepository;

  @Override
  public Set<CartResponse> getCart(String userId) {
    return userRepository.findById(userId).orElseThrow().getCart().stream().map(CartResponse::new)
        .collect(java.util.stream.Collectors.toSet());
  }

  @Override
  public CartResponse addToCart(CreateCartRequest request) {
    Cart cart = request.toCart();
    User buyer = userRepository.findById(request.getBuyerId()).orElseThrow();
    cart.setBuyer(buyer);
    ProductListing listing = listingRepository.findById(cart.getId().getListingId())
        .orElseThrow();
    cart.setListing(listing);
    return new CartResponse(cartRepository.save(cart));
  }

  @Override
  public void removeFromCart(DeleteCartRequest request) {
    Cart.CartId cartId = new Cart.CartId(request.getBuyerId(),
        new ProductListing.ProductListingId(request.getSellerId(), request.getProductId()));
    cartRepository.deleteById(cartId);
  }

  @Override
  @Transactional
  public void updateCartAmount(CreateCartRequest request) {
    if (request.getAmount() <= 0) {
      removeFromCart(new DeleteCartRequest(request.getBuyerId(), request.getSellerId(), request.getProductId()));
      return;
    }
    Cart cart = request.toCart();
    cart.setAmount(request.getAmount());
  }

  @Override
  public void clearCart(String userId) {
    cartRepository.deleteAllById_BuyerId(userId);
  }
}