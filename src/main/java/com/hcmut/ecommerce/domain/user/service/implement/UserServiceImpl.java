package com.hcmut.ecommerce.domain.user.service.implement;

import java.util.Set;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.hcmut.ecommerce.domain.cart.dto.request.CreateCartRequest;
import com.hcmut.ecommerce.domain.cart.dto.request.DeleteCartRequest;
import com.hcmut.ecommerce.domain.cart.dto.response.CartResponse;
import com.hcmut.ecommerce.domain.cart.model.Cart;
import com.hcmut.ecommerce.domain.cart.repository.CartRepository;
import com.hcmut.ecommerce.domain.productListing.model.ProductListing;
import com.hcmut.ecommerce.domain.productListing.repository.ProductListingRepository;
import com.hcmut.ecommerce.domain.user.dto.request.FirstLoginInforRequest;
import com.hcmut.ecommerce.domain.user.model.Buyer;
import com.hcmut.ecommerce.domain.user.model.Seller;
import com.hcmut.ecommerce.domain.user.model.User;
import com.hcmut.ecommerce.domain.user.model.User.UserRole;
import com.hcmut.ecommerce.domain.user.repository.UserRepository;
import com.hcmut.ecommerce.domain.user.service.interfaces.UserService;
import com.hcmut.ecommerce.domain.wallet.model.Wallet;

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

  public Buyer buyerLogin(GoogleIdToken.Payload payload){
    User user = userRepository.findByEmail(payload.getEmail()).orElseGet(() ->
                {
                    Wallet wallet = Wallet.builder()
                            .amount(0f)
                            .build();

                    User newBuyer = (Buyer) Buyer.builder()
                            .email(payload.getEmail())
                            .name((String) payload.get("name"))
                            .picture((String) payload.get("picture"))
                            .userRole(UserRole.BUYER)
                            .wallet(wallet)
                            .build();

                    wallet.setUser(newBuyer);

                    return userRepository.save(newBuyer);
                }
        );

        if (!(user instanceof Buyer)) {
            throw new IllegalStateException("This is not Buyer's account!");
        }

        return (Buyer) user;

  }

  public Seller sellerLogin(GoogleIdToken.Payload payload){
    log.info("Whatt");
    User user = userRepository.findByEmail(payload.getEmail()).orElseGet(() ->
                {
                    Wallet wallet = Wallet.builder()
                            .amount(0f)
                            .build();

                    Seller newSeller = Seller.builder()
                            .email(payload.getEmail())
                            .name((String) payload.get("name"))
                            .picture((String) payload.get("picture"))
                            .userRole((UserRole.SELLER))
                            .wallet(wallet)
                            .build();

                    wallet.setUser(newSeller);

                    log.info("Running login hereee");

                    return userRepository.save(newSeller);
                }
        );

        if (!(user instanceof Seller)) {
            throw new IllegalStateException("This is not Seller's account!");
        }
        

        return (Seller) user;

  }

  public User getMyInfor() {
        SecurityContext context = SecurityContextHolder.getContext();

        String email = context.getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        return user;
    }

  public User updateFirstLoginInfor(FirstLoginInforRequest request){
    User user = getMyInfor();
    user.setProvince(request.getProvince());
    user.setDistrict(request.getDistrict());
    user.setAddress(request.getAddress());
    user.setWard(request.getWard());
    user.setTel(request.getTel());
    return userRepository.save(user);
  }
}