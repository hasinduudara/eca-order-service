package com.eca.shop.order_service.controller;

import com.eca.shop.order_service.dto.OrderRequest;
import com.eca.shop.order_service.entity.Order;
import com.eca.shop.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * REST endpoint to place a new order.
     *
     * @param orderRequest The order details received from the client
     * @return A success message containing the generated order number
     */
    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest) {

        // Call the service layer to process and save the order
        String response = orderService.placeOrder(orderRequest);

        // Return 201 Created status with the response message
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Fetch all orders.
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    /**
     * Fetch a specific order by order number.
     */
    @GetMapping("/{orderNumber}")
    public ResponseEntity<Order> getOrderByOrderNumber(@PathVariable String orderNumber) {
        try {
            return ResponseEntity.ok(orderService.getOrderByOrderNumber(orderNumber));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}