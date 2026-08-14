package com.eca.shop.order_service.repository;

import com.eca.shop.order_service.entity.Order;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends FirestoreReactiveRepository<Order> {
}