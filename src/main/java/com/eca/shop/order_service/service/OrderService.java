package com.eca.shop.order_service.service;

import com.eca.shop.order_service.dto.OrderLineItemsDto;
import com.eca.shop.order_service.dto.OrderRequest;
import com.eca.shop.order_service.dto.ProductResponse;
import com.eca.shop.order_service.entity.Order;
import com.eca.shop.order_service.entity.OrderLineItems;
import com.eca.shop.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public String placeOrder(OrderRequest orderRequest) {

        // 1. Check stock availability for all items before placing the order
        for (OrderLineItemsDto item : orderRequest.getOrderLineItemsDtoList()) {

            // Call product-service using its Eureka registered name
            ProductResponse product = webClientBuilder.build().get()
                    .uri("http://product-service/api/v1/products/" + item.getProductId())
                    .retrieve()
                    .bodyToMono(ProductResponse.class)
                    .block(); // Synchronous call

            // Check if product exists and has enough stock
            if (product == null || product.getStockQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException("Product with ID " + item.getProductId() + " is out of stock or insufficient quantity!");
            }
        }

        // 2. If stock is available, proceed to place the order
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setUserId(orderRequest.getUserId());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        order.setOrderLineItemsList(orderLineItems);

        // Calculate total price
        double totalPrice = orderLineItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);
        log.info("Order Placed Successfully with Order Number: {}", order.getOrderNumber());
        return "Order Placed Successfully with Order Number: " + order.getOrderNumber();
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        return OrderLineItems.builder()
                .productId(orderLineItemsDto.getProductId())
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .build();
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getOrderNumber().equals(orderNumber))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order not found with order number: " + orderNumber));
    }
}