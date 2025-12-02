package com.hcmut.ecommerce.domain.productListing.service.implement;

import com.hcmut.ecommerce.domain.product.model.Product;
import com.hcmut.ecommerce.domain.product.repository.ProductRepository;
import com.hcmut.ecommerce.domain.productListing.dto.request.CreateProductListingRequest;
import com.hcmut.ecommerce.domain.productListing.dto.response.ProductListingResponse;
import com.hcmut.ecommerce.domain.productListing.model.ProductListing;
import com.hcmut.ecommerce.domain.productListing.model.ProductListing.ProductListingId;
import com.hcmut.ecommerce.domain.productListing.repository.ProductListingRepository;
import com.hcmut.ecommerce.domain.productListing.service.interfaces.ProductListingService;
import com.hcmut.ecommerce.domain.user.model.Seller;
import com.hcmut.ecommerce.domain.user.model.User;
import com.hcmut.ecommerce.domain.user.repository.UserRepository;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductListingServiceImpl implements ProductListingService {

  private final ProductListingRepository productListingRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final ProductListingSpecs productListingSpecs;
  // Use path-style URL for simpler development with minio
  private final S3Template s3Template;
  private final String productListingImageFolder = "product-listing-images";
  @Value("${spring.cloud.aws.s3.bucket-name}")
  private final String bucketName;
  @Value("${spring.cloud.aws.s3.endpoint}")
  private final String bucketHost;

  public ProductListingServiceImpl(ProductListingRepository productListingRepository,
      UserRepository userRepository,
      ProductRepository productRepository,
      ProductListingSpecs productListingSpecs,
      S3Template s3Template,
      @Value("${spring.cloud.aws.s3.bucket-name}") String bucketName,
      @Value("${spring.cloud.aws.s3.endpoint}") String bucketHost) {
    this.productListingRepository = productListingRepository;
    this.userRepository = userRepository;
    this.productRepository = productRepository;
    this.productListingSpecs = productListingSpecs;
    this.s3Template = s3Template;
    this.bucketName = bucketName;
    this.bucketHost = bucketHost;
  }

  @Override
  public ProductListingResponse getProductListingById(String sellerId, Long productId) throws Exception {
    ProductListingId id = new ProductListingId(sellerId, productId);
    ProductListing productListing = productListingRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("ProductListing not found with id: "
            + id));
    return new ProductListingResponse(productListing);
  }

  @Override
  public Page<ProductListingResponse> getAllProductListings(Integer pageSize, Integer page, String sortBy, Boolean desc,
      String keyword, Integer minPrice, Integer maxPrice, String categoryName, String province)
      throws Exception {
    Pageable pageable = PageRequest.of(page, pageSize,
        desc ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
    Specification<ProductListing> spec = Specification.unrestricted();
    if (minPrice != null || maxPrice != null) {
      if (minPrice == null)
        minPrice = 0;
      if (maxPrice == null)
        maxPrice = Integer.MAX_VALUE;
      spec = spec.and(productListingSpecs.hasPriceBetween(minPrice, maxPrice));
    }
    if (categoryName != null) {
      spec = spec.and(productListingSpecs.hasCategory(categoryName));
    }
    if (province != null) {
      spec = spec.and(productListingSpecs.hasProvince(province));
    }
    if (keyword != null) {
      spec = spec.and(productListingSpecs.hasKeyword(keyword));
    }
    Page<ProductListing> productListings = productListingRepository.findAll(spec, pageable);
    return productListings.map(ProductListingResponse::new);
  }

  @Override
  public ProductListingResponse createProductListing(CreateProductListingRequest request) throws Exception {
    ProductListing productListing = request.toProductListing();
    User user = userRepository.findById(request.getSellerId()).orElseThrow();
    if (!(user instanceof Seller)) {
      throw new RuntimeException("User is not a seller");
    }
    Seller seller = (Seller) user;
    productListing.setSeller(seller);
    Product product = productRepository.findById(request.getProductId()).orElseThrow();
    productListing.setProduct(product);
    // Format: product-listing-images/{sellerId}/{productId}/image_1.png
    // image/png, image/jpg, etc.
    String imageExtension = "." + productListing.getImageType().split("/")[1];
    String imageUrl = productListingImageFolder + "/" + seller.getId() + "/" + product.getId() + "/" + "image_1"
        + imageExtension;
    byte[] imageBytes = Base64.getDecoder().decode(request.getImageBlobString());
    s3Template.upload(bucketName, imageUrl, new ByteArrayInputStream(imageBytes),
        ObjectMetadata.builder()
            .contentType(productListing.getImageType())
            .contentLength((long) imageBytes.length)
            .build());
    productListing.setImageUrl(bucketHost + "/" + bucketName + "/" + imageUrl);
    return new ProductListingResponse(productListingRepository.save(productListing));
  }
}
