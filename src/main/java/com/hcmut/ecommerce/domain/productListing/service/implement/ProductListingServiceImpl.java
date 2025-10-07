package com.hcmut.ecommerce.domain.productListing.service.implement;

import com.hcmut.ecommerce.domain.product.model.Product;
import com.hcmut.ecommerce.domain.product.repository.ProductRepository;
import com.hcmut.ecommerce.domain.productListing.dto.request.CreateProductListingRequest;
import com.hcmut.ecommerce.domain.productListing.dto.response.ProductListingResponse;
import com.hcmut.ecommerce.domain.productListing.model.ProductListing;
import com.hcmut.ecommerce.domain.productListing.repository.ProductListingRepository;
import com.hcmut.ecommerce.domain.productListing.service.interfaces.ProductListingService;
import com.hcmut.ecommerce.domain.user.model.User;
import com.hcmut.ecommerce.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductListingServiceImpl implements ProductListingService {

  private final ProductListingRepository productListingRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;

  // @Override
  // public ProductListingResponse getProductListingById(Long id) throws Exception
  // {
  // ProductListing productListing = productListingRepository.findById(id)
  // .orElseThrow(() -> new RuntimeException("ProductListing not found with id: "
  // + id));
  // return new ProductListingResponse(productListing);
  // }

  @Override
  public Page<ProductListingResponse> getAllProductListings(Integer pageSize, Integer page, String sortBy, Boolean desc)
      throws Exception {
    Pageable pageable = PageRequest.of(page, pageSize,
        desc ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
    Page<ProductListing> productListings = productListingRepository.findAll(pageable);
    return productListings.map(ProductListingResponse::new);
  }

  @Override
  public ProductListingResponse createProductListing(CreateProductListingRequest request) throws Exception {
    ProductListing productListing = request.toProductListing();
    User seller = userRepository.findById(request.getSellerId()).orElseThrow();
    productListing.setSeller(seller);
    Product product = productRepository.findById(request.getProductId()).orElseThrow();
    productListing.setProduct(product);
    return new ProductListingResponse(productListingRepository.save(productListing));
  }
}