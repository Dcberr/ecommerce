package com.hcmut.ecommerce.domain.order.repository;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmut.ecommerce.domain.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, String> {

}
