package com.hcmut.ecommerce.domain.payment.model;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hcmut.ecommerce.domain.order.model.Order;
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
@Table(name = "escrows")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Escrow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Integer amount;

    @Enumerated(EnumType.STRING)
    private EscrowStatus escrowStatus;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "buyer_id", nullable = false)
    @JsonBackReference
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id", nullable = false)
    @JsonBackReference
    private User seller;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id", unique = true)
    @JsonBackReference
    private Order order;

    private LocalDateTime createdAt;

    private LocalDateTime releaseAt;

    public enum EscrowStatus {
        WAITING ,HOLDING, RELEASE
    }
    
}
