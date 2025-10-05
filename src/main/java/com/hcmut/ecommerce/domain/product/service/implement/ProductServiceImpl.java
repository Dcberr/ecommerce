package com.hcmut.ecommerce.domain.product.service.implement;

import com.hcmut.ecommerce.domain.category.model.Category;
import com.hcmut.ecommerce.domain.category.repository.CategoryRepository;
import com.hcmut.ecommerce.domain.product.dto.request.CreateProductRequest;
import com.hcmut.ecommerce.domain.product.dto.response.ProductResponse;
import com.hcmut.ecommerce.domain.product.model.Product;
import com.hcmut.ecommerce.domain.product.repository.ProductRepository;
import com.hcmut.ecommerce.domain.product.service.interfaces.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

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
    return new ProductResponse(productRepository.save(product));
  }
}