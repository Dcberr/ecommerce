package com.hcmut.ecommerce.domain.productListing.service.implement;

import com.hcmut.ecommerce.domain.productListing.model.ProductListing;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ProductListingSpecs {
  public Specification<ProductListing> hasPriceBetween(Integer minPrice, Integer maxPrice) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
  }

  public Specification<ProductListing> hasCategory(String categoryName) {
    return (root, query, criteriaBuilder) -> {
      var categoriesJoin = root.join("product").join("categories");
      return criteriaBuilder.equal(categoriesJoin.get("name"), categoryName);
    };
  }

  public Specification<ProductListing> hasProvince(String province) {
    return (root, query, criteriaBuilder) -> {
      var userJoin = root.join("seller");
      return criteriaBuilder.equal(userJoin.get("province"), province);
    };
  }

  public Specification<ProductListing> hasKeyword(String keyword) {
    return (root, query, criteriaBuilder) -> {
      var productJoin = root.join("product");
      String pattern = "%" + keyword.toLowerCase() + "%";
      return criteriaBuilder.or(
          criteriaBuilder.like(criteriaBuilder.lower(productJoin.get("name")), pattern),
          criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
          criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern));
    };
  }

}
