package com.testdrivendevelopment.OrderManagementSystem.repository;

import com.testdrivendevelopment.OrderManagementSystem.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
