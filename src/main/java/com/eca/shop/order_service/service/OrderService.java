package com.eca.shop.order_service.service;

import com.eca.shop.order_service.dto.OrderLineItemsDto;
import com.eca.shop.order_service.dto.OrderRequest;
import com.eca.shop.order_service.entity.Order;
import com.eca.shop.order_service.entity.OrderLineItems;
import com.eca.shop.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        // අහඹු අංකයක් (UUID) Order Number එක ලෙස ලබා දීම
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setUserId(orderRequest.getUserId());

        // DTO එක Entity එකක් බවට පරිවර්තනය කිරීම (Mapping)
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        order.setOrderLineItemsList(orderLineItems);

        // මුළු මුදල ගණනය කිරීම (මිල x ප්‍රමාණය)
        double totalPrice = orderLineItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        order.setTotalPrice(totalPrice);

        // Database එකට Save කිරීම
        orderRepository.save(order);

        return "Order Placed Successfully with Order Number: " + order.getOrderNumber();
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        return OrderLineItems.builder()
                .productId(orderLineItemsDto.getProductId())
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .build();
    }

    /**
     * Retrieves all orders from the database.
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Retrieves a specific order by its order number.
     */
    public Order getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getOrderNumber().equals(orderNumber))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order not found with order number: " + orderNumber));
    }
}