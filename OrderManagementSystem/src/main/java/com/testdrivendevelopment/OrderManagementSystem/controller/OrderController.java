package com.testdrivendevelopment.OrderManagementSystem.controller;

import com.testdrivendevelopment.OrderManagementSystem.model.Order;
import com.testdrivendevelopment.OrderManagementSystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    @Autowired
    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    //READ
    @GetMapping
    ResponseEntity<List<Order>> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return new ResponseEntity<>(orderService.getOrderById(id), HttpStatus.OK);
    }
//
    //CREATE
//    @PostMapping
//    ResponseEntity<Order> createOrder(@RequestBody Order order) {
//        return new ResponseEntity<>(orderService.createOrder(order), HttpStatus.CREATED);
//    }
//
//    //UPDATE
//    @PutMapping("/{id}")
//    public Order updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
//        return orderService.updateOrder(id, updatedOrder);
//    }
//
//    //DELETE
//    @DeleteMapping("/{id}")
//    public List<Order> deleteOrder(@PathVariable Long id) {
//        return orderService.deleteOrder(id);
//    }
}
