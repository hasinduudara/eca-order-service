package com.eca.shop.order_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import com.google.cloud.spring.data.firestore.Document;

import java.util.List;

@Document(collectionName = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Order {
    @Id
    private String id;
    private String orderNumber;
    private Long userId;
    private List<OrderLineItems> orderLineItemsList;
    private Double totalPrice;
}