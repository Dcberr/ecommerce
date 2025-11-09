package com.hcmut.ecommerce.domain.order.repository;

import java.util.List;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmut.ecommerce.domain.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findOrderBySellerId(String sellerId);
}
