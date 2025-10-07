package com.hcmut.ecommerce.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmut.ecommerce.domain.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}