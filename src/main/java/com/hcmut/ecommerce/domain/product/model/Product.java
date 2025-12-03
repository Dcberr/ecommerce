package com.hcmut.ecommerce.domain.product.model;

import java.util.Set;

import com.hcmut.ecommerce.domain.category.model.Category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NonNull
  @Column(unique = true)
  private String name;

  @NonNull
  @Enumerated(EnumType.STRING)
  @Column
  private ProductUnitType baseUnit;

  private String imageUrl;
  private String imageType;

  // @NonNull
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "product_categories", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
  private Set<Category> categories;

  public enum ProductUnitType {
    PIECE,
    KILOGRAM,
    LITER,
  }

}
