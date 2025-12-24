package com.hcmut.ecommerce.domain.order.service.implement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hcmut.ecommerce.domain.order.dto.request.CreateOrderRequest;
import com.hcmut.ecommerce.domain.order.dto.request.FeeRequest;
import com.hcmut.ecommerce.domain.order.model.Order;
import com.hcmut.ecommerce.domain.order.model.Order.OrderStatus;
import com.hcmut.ecommerce.domain.order.model.OrderItem;
import com.hcmut.ecommerce.domain.order.repository.OrderRepository;
import com.hcmut.ecommerce.domain.order.service.interfaces.DeliveryService;
import com.hcmut.ecommerce.domain.order.service.interfaces.OrderService;
import com.hcmut.ecommerce.domain.payment.model.Escrow;
import com.hcmut.ecommerce.domain.payment.model.Escrow.EscrowStatus;
import com.hcmut.ecommerce.domain.payment.repository.EscrowRepository;
import com.hcmut.ecommerce.domain.payment.service.interfaces.EscrowService;
import com.hcmut.ecommerce.domain.product.repository.ProductRepository;
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
    private final EscrowService escrowService;
    private final EscrowRepository escrowRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final DeliveryService deliveryService;
    private final ProductRepository productRepository;

    @Override
    public Order createOrder(CreateOrderRequest request) throws JsonMappingException, JsonProcessingException {

        // ===== 1. Buyer & Seller =====
        User buyer = userService.getMyInfor();

        // User seller = userRepository.findById(request.getSellerId())
        //     .orElseThrow(() -> new RuntimeException("Seller not found"));
        List<OrderItem> orderItems = new ArrayList<>();
        String sellerId = null;

        for (var itemReq : request.getItems()) {

            var product = productRepository.findById(itemReq.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

            if (sellerId == null) {
                sellerId = product.getSellerId();
            } else if (!sellerId.equals(product.getSellerId())) {
                throw new RuntimeException("1 order chỉ được chứa 1 seller");
            }

            long price = product.getPrice();
            int discount = product.getDiscount();
            int qty = itemReq.getQuantity();
            long finalPrice = price * qty * (100 - discount) / 100;

            orderItems.add(
                OrderItem.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .productImage(product.getImageUrl())
                    .productUnit(product.getBaseUnit().name())
                    .price(price)
                    .discount(discount)
                    .quantity(qty)
                    .finalPrice(finalPrice)
                    .build()
            );
        }
        User seller = userRepository.findById(sellerId)
             .orElseThrow(() -> new RuntimeException("Seller not found"));

        // ===== 3. Tính tổng tiền =====
        int totalProductPrice = orderItems.stream()
            .mapToInt(it -> it.getFinalPrice().intValue())
            .sum();

        // ===== 4. Shipping fee =====
        Integer shippingFee = deliveryService.getShippingFee(
            FeeRequest.builder()
                .pickProvince(seller.getProvince())
                .pickDistrict(seller.getDistrict())
                .province(buyer.getProvince())
                .district(buyer.getDistrict())
                .address(buyer.getAddress())
                .weight(1000)
                .value(totalProductPrice)
                .transport("road")
                .build()
        ).getFee();

        log.info("Shipping fee = {}", shippingFee);

        // ===== 5. Build Order =====
        Order order = Order.builder()
            .buyer(buyer)
            .seller(seller)
            .items(orderItems)
            .totalProductPrice(totalProductPrice)
            .shippingFee(shippingFee)
            .pick_money(totalProductPrice + shippingFee)
            .orderStatus(OrderStatus.WAITING)
            .note(request.getNote())
            .transport("road")
            .createdAt(LocalDateTime.now())

            // buyer info
            .name(buyer.getName())
            .tel(buyer.getTel())
            .address(buyer.getAddress())
            .province(buyer.getProvince())
            .district(buyer.getDistrict())
            .ward(buyer.getWard())

            // seller info
            .pick_name(seller.getName())
            .pick_tel(seller.getTel())
            .pick_address(seller.getAddress())
            .pick_province(seller.getProvince())
            .pick_district(seller.getDistrict())
            .pick_ward(seller.getWard())
            .is_freeship("0")
            .build();
        

        // ===== 6. Gắn order vào từng OrderItem =====
        orderItems.forEach(item -> item.setOrder(order));

        // ===== 7. Create Escrow =====
        Escrow escrow = Escrow.builder()
            .order(order)
            .buyer(buyer)
            .seller(seller)
            .amount(totalProductPrice + shippingFee)
            .escrowStatus(EscrowStatus.WAITING)
            .createdAt(LocalDateTime.now())
            .build();

        order.setEscrow(escrow);

        // ===== 8. Save (cascade ALL) =====
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(String id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Order> getOrderBySellerId() {
        return orderRepository.findOrderBySellerId(
            userService.getMyInfor().getId()
        );
    }

    public List<Order> getOrderByBuyerId() {
        return orderRepository.findOrderByBuyerId(
            userService.getMyInfor().getId()
        );
    }

    public List<Order> getAllOrders() { return orderRepository.findAll(); }

    public Order updateOrder(String id, Integer totalAmount) { Order order = getOrderById(id); order.setPick_money(totalAmount); return orderRepository.save(order); } 
    
    public void deleteOrder(String id) { orderRepository.deleteById(id); }

    public void confirmOrder(String id) {
        Order order = getOrderById(id);
        order.setOrderStatus(OrderStatus.DELIVERING);
        orderRepository.save(order);
    }

    public void completeOrder(String id) {
        Order order = getOrderById(id);
        order.setOrderStatus(OrderStatus.DELIVERED);
        Escrow escrow = order.getEscrow();
        escrow.setEscrowStatus(EscrowStatus.RELEASE);
        escrow.setReleaseAt(LocalDateTime.now());
        escrowRepository.save(escrow);
        orderRepository.save(order);
    }

    public void cancelOrder(String id) {
        Order order = getOrderById(id);
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
