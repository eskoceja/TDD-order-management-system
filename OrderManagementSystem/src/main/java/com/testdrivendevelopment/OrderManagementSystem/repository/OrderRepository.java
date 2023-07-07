package com.testdrivendevelopment.OrderManagementSystem.repository;

import com.testdrivendevelopment.OrderManagementSystem.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Repository extends from JpaRepository, which is a CRUD Repository
//with more specific functionality for what we want to achieve
//tests for the repository can be found in:
//'OrderManagementSystem/src/test/java/com/testdrivendevelopment/OrderManagementSystem/repository/OrderRepositoryTest.java'

@Repository("orderRepository")
public interface OrderRepository extends JpaRepository<Order, Long> {
}
