package com.hcmut.ecommerce.domain.order.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hcmut.ecommerce.domain.payment.model.Escrow;
import com.hcmut.ecommerce.domain.user.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    private String pick_name;
        private String pick_address;
        private String pick_province;
        private String pick_district;
        private String pick_ward;
        private String pick_tel;
        private String tel;
        private String name;
        private String address;
        private String province;
        private String district;
        private String ward;
        private String hamlet;
        private String transport;  
        private Integer pick_money;
        private Integer value;      
        private String note;
        private String is_freeship; 
    
    private Integer shippingFee;
    private Integer totalProductPrice;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "buyer_id", nullable = false)
    @JsonBackReference
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id", nullable = false)
    @JsonBackReference
    private User seller;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // private Float totalAmount;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Escrow escrow;

    public enum OrderStatus {
        UNPAID, DELIVERING, DELIVERED, CANCELLED
    }

}
