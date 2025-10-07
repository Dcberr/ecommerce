package com.hcmut.ecommerce.domain.user.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hcmut.ecommerce.domain.order.model.Order;
import com.hcmut.ecommerce.domain.payment.model.Escrow;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("SELLER")
@Getter
@Setter
@NoArgsConstructor        
@AllArgsConstructor
@SuperBuilder
public class Seller extends User {
    // private String shopName;
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Order> soldOrders = new HashSet<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Escrow> sellerEscrows = new HashSet<>();
    
}
