package com.hcmut.ecommerce.domain.order.service.implement;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hcmut.ecommerce.domain.order.dto.request.CreateOrderRequest;
import com.hcmut.ecommerce.domain.order.model.Order;
import com.hcmut.ecommerce.domain.order.model.Order.OrderStatus;
import com.hcmut.ecommerce.domain.order.repository.OrderRepository;
import com.hcmut.ecommerce.domain.order.service.interfaces.OrderService;
import com.hcmut.ecommerce.domain.payment.model.Escrow;
import com.hcmut.ecommerce.domain.payment.model.Escrow.EscrowStatus;
import com.hcmut.ecommerce.domain.payment.repository.EscrowRepository;
import com.hcmut.ecommerce.domain.payment.service.interfaces.EscrowService;
import com.hcmut.ecommerce.domain.user.model.User;
import com.hcmut.ecommerce.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final EscrowRepository escrowRepository;
    private final EscrowService escrowService;
    private final UserRepository userRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
    }

    public Order createOrder(CreateOrderRequest request) {
        // Escrow escrow = escrowRepository.findById(request.getEscrowId())
        //         .orElseThrow(() -> new RuntimeException("Escrow not found with id " + request.getEscrowId()));

        User buyer = userRepository.findById(request.getBuyerId())
                    .orElseThrow(() -> new RuntimeException("User not found with id " + request.getBuyerId()));

        User seller = userRepository.findById(request.getSellerId())
                    .orElseThrow(() -> new RuntimeException("User not found with id " + request.getBuyerId()));
        
        Order order = Order.builder()
                .totalAmount(request.getTotalAmount())
                .buyer(buyer)
                .seller(seller)
                .orderStatus(OrderStatus.UNPAID)
                .build();

        Escrow escrow = Escrow.builder()
                .amount(request.getTotalAmount())
                .buyer(buyer)
                .seller(seller)
                .escrowStatus(EscrowStatus.WAITING)
                .createdAt(LocalDateTime.now())
                .build();
        
        order.setEscrow(escrow);
        escrow.setOrder(order);

        return orderRepository.save(order);
    }

    public Order updateOrder(String id, Float totalAmount) {
        Order order = getOrderById(id);
        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }

    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }
}
