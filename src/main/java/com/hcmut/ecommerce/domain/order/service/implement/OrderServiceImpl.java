package com.hcmut.ecommerce.domain.order.service.implement;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hcmut.ecommerce.domain.order.dto.request.CreateOrderRequest;
import com.hcmut.ecommerce.domain.order.dto.request.FeeRequest;
import com.hcmut.ecommerce.domain.order.model.Order;
import com.hcmut.ecommerce.domain.order.model.Order.OrderStatus;
import com.hcmut.ecommerce.domain.order.repository.OrderRepository;
import com.hcmut.ecommerce.domain.order.service.interfaces.DeliveryService;
import com.hcmut.ecommerce.domain.order.service.interfaces.OrderService;
import com.hcmut.ecommerce.domain.payment.model.Escrow;
import com.hcmut.ecommerce.domain.payment.model.Escrow.EscrowStatus;
import com.hcmut.ecommerce.domain.payment.repository.EscrowRepository;
import com.hcmut.ecommerce.domain.payment.service.interfaces.EscrowService;
import com.hcmut.ecommerce.domain.user.model.User;
import com.hcmut.ecommerce.domain.user.repository.UserRepository;
import com.hcmut.ecommerce.domain.user.service.interfaces.UserService;

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
    private final UserService userService;
    private final DeliveryService deliveryService;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
    }

    public Order createOrder(CreateOrderRequest request) throws JsonMappingException, JsonProcessingException {
        // Escrow escrow = escrowRepository.findById(request.getEscrowId())
        //         .orElseThrow(() -> new RuntimeException("Escrow not found with id " + request.getEscrowId()));

        // User buyer = userRepository.findById(request.getBuyerId())
        //             .orElseThrow(() -> new RuntimeException("User not found with id " + request.getBuyerId()));
        User buyer = userService.getMyInfor();

        User seller = userRepository.findById(request.getSellerId())
                    .orElseThrow(() -> new RuntimeException("User not found with id " + request.getBuyerId()));

        Integer shippingFee = deliveryService.getShippingFee(
            FeeRequest.builder()
                .pickProvince(seller.getProvince())
                .pickDistrict(seller.getDistrict())
                .province(buyer.getProvince())
                .district(buyer.getDistrict())
                .address(buyer.getAddress())
                .weight(1000)
                .value(request.getTotalProductPrice())
                .transport("road")
                .build()
        ).getFee();
        log.info("Shipping fee: " + shippingFee);
        
        Order order = Order.builder()
                .pick_money(request.getTotalProductPrice() + shippingFee)
                .shippingFee(
                    shippingFee
                )
                .totalProductPrice(request.getTotalProductPrice())
                .buyer(buyer)
                .name(buyer.getName())
                .province(buyer.getProvince())
                .district(buyer.getDistrict())
                .tel(buyer.getTel())
                .address(buyer.getAddress())
                .ward(buyer.getWard())
                .seller(seller)
                .pick_name(seller.getName())
                .pick_address(seller.getAddress())
                .pick_district(seller.getDistrict())
                .pick_tel(seller.getTel())
                .pick_province(seller.getProvince())
                .pick_ward(seller.getWard())
                .is_freeship("0")
                .value(request.getTotalProductPrice())
                .note(request.getNote())
                .hamlet("Khác")
                .transport("road")
                .orderStatus(OrderStatus.UNPAID)
                .build();

        Escrow escrow = Escrow.builder()
                .amount(request.getTotalProductPrice() + shippingFee)
                .buyer(buyer)
                .seller(seller)
                .escrowStatus(EscrowStatus.WAITING)
                .createdAt(LocalDateTime.now())
                .build();
        
        order.setEscrow(escrow);
        escrow.setOrder(order);

        return orderRepository.save(order);
    }

    public Order updateOrder(String id, Integer totalAmount) {
        Order order = getOrderById(id);
        order.setPick_money(totalAmount);
        return orderRepository.save(order);
    }

    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }

    public List<Order> getOrderBySellerId(){
        return orderRepository.findOrderBySellerId(userService.getMyInfor().getId());
    }

    
}
