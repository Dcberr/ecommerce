package com.hcmut.ecommerce.domain.order.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hcmut.ecommerce.domain.product.model.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== Quan hệ =====
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    // ===== Snapshot Product =====
    private Long productId;          // ID product lúc mua
    private String productName;
    private String productImage;
    private String productUnit;

    // ===== Giá tại thời điểm mua =====
    private Long price;              // giá gốc
    private Integer discount;        // %
    private Integer quantity;
    private Long finalPrice;         // price * qty * (100-discount)/100
}

