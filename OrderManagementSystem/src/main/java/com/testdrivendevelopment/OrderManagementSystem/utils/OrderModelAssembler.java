package com.testdrivendevelopment.OrderManagementSystem.utils;

import com.testdrivendevelopment.OrderManagementSystem.controller.OrderController;
import com.testdrivendevelopment.OrderManagementSystem.model.Order;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {
    @Override
    public EntityModel<Order> toModel(Order order) {
        EntityModel<Order> orderModel = EntityModel.of(order,
                linkTo(methodOn(OrderController.class).getOrderById(order.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).updateOrder(order.getId(), order)).withRel("update"),
                linkTo(methodOn(OrderController.class).deleteOrder(order.getId())).withRel("delete")
        );
        orderModel.add(linkTo(methodOn(OrderController.class).getOrderById(order.getId())).withRel("id"));
        orderModel.getContent().setId(order.getId());
        return orderModel;
    }
}