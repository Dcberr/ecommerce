package com.hcmut.ecommerce.domain.category.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hcmut.ecommerce.domain.product.model.Product;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NonNull
  private String name;

  private String description;

  @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
  @JsonIgnore
  private Set<Product> products;

}