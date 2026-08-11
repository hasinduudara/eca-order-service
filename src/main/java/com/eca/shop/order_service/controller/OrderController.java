package com.eca.shop.order_service.controller;

import com.eca.shop.order_service.dto.OrderRequest;
import com.eca.shop.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}