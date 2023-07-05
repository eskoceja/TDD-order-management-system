package com.testdrivendevelopment.OrderManagementSystem.repository;

import com.testdrivendevelopment.OrderManagementSystem.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
    }

    @Test
    void shouldSaveOrder() {
        //Given
        Order order = new Order("Fiona", LocalDate.now(), "123 Swamp Ave", 40.45);

        //When
        Order savedOrder = orderRepository.save(order);

        //Then
        Optional<Order> optionalOrder = orderRepository.findById(savedOrder.getId());
        assertThat(optionalOrder)
                .isPresent()
                .hasValueSatisfying(o -> {
                    assertThat(o).isEqualToComparingFieldByField(order);
                });
    }

    @Test
    void shouldFindOrderById() {
        // Given
        Order order = new Order("Fiona", LocalDate.now(), "123 Swamp Ave", 40.45);
        Order savedOrder = orderRepository.save(order);

        // When
        Optional<Order> optionalOrder = orderRepository.findById(savedOrder.getId());

        // Then
        assertThat(optionalOrder)
                .isPresent()
                .hasValueSatisfying(o -> {
                    assertThat(o).isEqualToComparingFieldByField(savedOrder);
                });
    }

    @Test
    void shouldUpdateOrder() {
        // Given
        Order order = new Order("Fiona", LocalDate.now(), "123 Swamp Ave", 40.45);
        Order savedOrder = orderRepository.save(order);

        // When
        savedOrder.setCustomerName("Updated Name");
        Order updatedOrder = orderRepository.save(savedOrder);

        // Then
        Optional<Order> optionalOrder = orderRepository.findById(updatedOrder.getId());
        assertThat(optionalOrder)
                .isPresent()
                .hasValueSatisfying(o -> {
                    assertThat(o).isEqualToComparingFieldByField(updatedOrder);
                });
    }

    @Test
    void shouldDeleteOrder() {
        // Given
        Order order = new Order("Fiona", LocalDate.now(), "123 Swamp Ave", 40.45);
        Order savedOrder = orderRepository.save(order);

        // When
        orderRepository.delete(savedOrder);

        // Then
        Optional<Order> optionalOrder = orderRepository.findById(savedOrder.getId());
        assertThat(optionalOrder).isEmpty();
    }

}
