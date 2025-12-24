package com.hcmut.ecommerce.domain.category.service.implement;

import com.hcmut.ecommerce.domain.category.dto.request.CreateCategoryRequest;
import com.hcmut.ecommerce.domain.category.dto.response.CategoryResponse;
import com.hcmut.ecommerce.domain.category.model.Category;
import com.hcmut.ecommerce.domain.category.repository.CategoryRepository;
import com.hcmut.ecommerce.domain.category.service.interfaces.CategoryService;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;
  private final S3Template s3Template;
  @Value("${spring.cloud.aws.s3.bucket-name}")
  private final String bucketName;
  @Value("${spring.cloud.aws.s3.endpoint}")
  private final String bucketHost;
  @Value("${spring.cloud.aws.s3.public-endpoint}")
  private final String publicBucketHost;
  private final String categoryImageFolder = "category-images";

  public CategoryServiceImpl(CategoryRepository categoryRepository, S3Template s3Template,
      @Value("${spring.cloud.aws.s3.bucket-name}") String bucketName,
      @Value("${spring.cloud.aws.s3.endpoint}") String bucketHost,
      @Value("${spring.cloud.aws.s3.public-endpoint}") String publicBucketHost) {
    this.categoryRepository = categoryRepository;
    this.s3Template = s3Template;
    this.bucketName = bucketName;
    this.bucketHost = bucketHost;
    this.publicBucketHost = publicBucketHost;
  }

  @Override
  public CategoryResponse getCategoryById(Long id) throws Exception {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    return new CategoryResponse(category);
  }

  @Override
  public List<CategoryResponse> getAllCategories() throws Exception {
    List<Category> categories = categoryRepository.findAll();
    return categories.stream().map(CategoryResponse::new).toList();
  }

  @Override
  public CategoryResponse createCategory(CreateCategoryRequest request) throws Exception {
    Category category = categoryRepository.save(request.toCategory());
    if (request.getImageBlobString() == null || request.getImageBlobString().isEmpty()) {
      return new CategoryResponse(category);
    }
    String imageExtension = "." + category.getImageType().split("/")[1];
    String imageUrl = categoryImageFolder + "/" + category.getId() + "/" + "image_1" + imageExtension;
    byte[] imageBytes = Base64.getDecoder().decode(request.getImageBlobString());
    s3Template.upload(bucketName, imageUrl, new ByteArrayInputStream(imageBytes),
        ObjectMetadata.builder()
            .contentLength((long) imageBytes.length)
            .contentType(category.getImageType())
            .build());
    category.setImageUrl(publicBucketHost + "/" + bucketName + "/" + imageUrl);
    return new CategoryResponse(categoryRepository.save(category));
  }
}