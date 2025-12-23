package com.hcmut.ecommerce.domain.product.service.implement;

import com.hcmut.ecommerce.domain.category.model.Category;
import com.hcmut.ecommerce.domain.category.repository.CategoryRepository;
import com.hcmut.ecommerce.domain.product.dto.request.CreateProductRequest;
import com.hcmut.ecommerce.domain.product.dto.response.ProductResponse;
import com.hcmut.ecommerce.domain.product.model.Product;
import com.hcmut.ecommerce.domain.product.repository.ProductRepository;
import com.hcmut.ecommerce.domain.product.service.interfaces.ProductService;
import com.hcmut.ecommerce.domain.user.service.interfaces.UserService;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.Set;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final UserService userService;
  private final S3Template s3Template;
  private final String productImageFolder = "product-images";
  @Value("${spring.cloud.aws.s3.bucket-name}")
  private final String bucketName;
  @Value("${spring.cloud.aws.s3.endpoint}")
  private final String bucketHost;

  public ProductServiceImpl(ProductRepository productRepository,
      CategoryRepository categoryRepository,
      UserService userService,
      S3Template s3Template,
      @Value("${spring.cloud.aws.s3.bucket-name}") String bucketName,
      @Value("${spring.cloud.aws.s3.endpoint}") String bucketHost) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
    this.userService = userService;
    this.s3Template = s3Template;
    this.bucketName = bucketName;
    this.bucketHost = bucketHost;
  }

  @Override
  public ProductResponse getProductById(Long id) throws Exception {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    return new ProductResponse(product);
  }

  @Override
  public Page<ProductResponse> getAllProducts(Integer pageSize, Integer page, String sortBy, Boolean desc)
      throws Exception {
    Pageable pageable = PageRequest.of(page, pageSize,
        desc ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
    Page<Product> products = productRepository.findAll(pageable);
    return products.map(ProductResponse::new);
  }

  @Override
  public ProductResponse createProduct(CreateProductRequest request) throws Exception {
    Product product = request.toProduct();
    Set<Category> categories = request.getCategoryIds().stream()
        .map(id -> categoryRepository.findById(Long.valueOf(id))
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id)))
        .collect(java.util.stream.Collectors.toSet());
    product.setCategories(categories);

    // Handle image blob upload if provided
    if (request.getImageBlobString() != null && request.getImageType() != null) {
      // Save the product first to get the generated ID
      product = productRepository.save(product);

      // Format: product-images/{productId}/image_1.png
      String imageExtension = "." + request.getImageType().split("/")[1];
      String imageUrl = productImageFolder + "/" + product.getId() + "/" + "image_1" + imageExtension;
      byte[] imageBytes = Base64.getDecoder().decode(request.getImageBlobString());
      s3Template.upload(bucketName, imageUrl, new ByteArrayInputStream(imageBytes),
          ObjectMetadata.builder()
              .contentType(request.getImageType())
              .contentLength((long) imageBytes.length)
              .build());
      product.setImageUrl(bucketHost + "/" + bucketName + "/" + imageUrl);
      log.info(userService.getMyInfor().getId());
      String sellerId = userService.getMyInfor().getId();

      product.setSellerId(sellerId);
      return new ProductResponse(productRepository.save(product));
    }

    return new ProductResponse(productRepository.save(product));
  }

  public Page<ProductResponse> getProductBySellerId(Integer pageSize, Integer page, String sortBy, Boolean desc) {
    String sellerId = userService.getMyInfor().getId();
    Pageable pageable = PageRequest.of(page, pageSize,
        desc ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
    Page<Product> products = productRepository.findBySellerId(sellerId, pageable);
    return products.map(ProductResponse::new);
  }
}