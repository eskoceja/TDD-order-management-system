package com.testdrivendevelopment.OrderManagementSystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testdrivendevelopment.OrderManagementSystem.model.Order;
import com.testdrivendevelopment.OrderManagementSystem.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)

class OrderControllerTest {
    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void getAllOrders_ShouldReturnListOfOrders() throws Exception {
        // Arrange
        List<Order> orders = Arrays.asList(
                new Order("John Doe", LocalDate.now(), "123 Main St", 100.0),
                new Order("Jane Smith", LocalDate.now(), "456 Elm St", 200.0)
        );
        Mockito.when(orderService.getAllOrders()).thenReturn(orders);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerName", is("John Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].shippingAddress", is("123 Main St")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].customerName", is("Jane Smith")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].shippingAddress", is("456 Elm St")));

        Mockito.verify(orderService, Mockito.times(1)).getAllOrders();
        Mockito.verifyNoMoreInteractions(orderService);
    }

    @Test
    public void createOrder_ShouldReturnCreatedOrder() throws Exception {
        // Arrange
        Order order = new Order("John Doe", LocalDate.now(), "123 Main St", 100.0);
        Mockito.when(orderService.createOrder(Mockito.any(Order.class))).thenReturn(order);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName", is("John Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shippingAddress", is("123 Main St")));

        Mockito.verify(orderService, Mockito.times(1)).createOrder(Mockito.any(Order.class));
        Mockito.verifyNoMoreInteractions(orderService);
    }

    @Test
    public void getOrderById_ShouldReturnCorrectOrder() throws Exception {
        // Arrange
        Long orderId = 1L;
        Order order = new Order("John Doe", LocalDate.now(), "123 Main St", 100.0);
        Mockito.when(orderService.getOrderById(orderId)).thenReturn(order);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/{id}", orderId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName", is("John Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shippingAddress", is("123 Main St")));

        Mockito.verify(orderService, Mockito.times(1)).getOrderById(orderId);
        Mockito.verifyNoMoreInteractions(orderService);
    }

    @Test
    public void updateOrder_ShouldReturnUpdatedOrder() throws Exception {
        // Arrange
        Long orderId = 1L;
        Order updatedOrder = new Order("Jane Smith", LocalDate.now(), "456 Elm St", 200.0);
        Order existingOrder = new Order("John Doe", LocalDate.now(), "123 Main St", 100.0);
        Mockito.when(orderService.updateOrder(orderId, updatedOrder)).thenReturn(existingOrder);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedOrder)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName", is("John Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shippingAddress", is("123 Main St")));

        Mockito.verify(orderService, Mockito.times(1)).updateOrder(orderId, updatedOrder);
        Mockito.verifyNoMoreInteractions(orderService);
    }

    @Test
    public void deleteOrder_ShouldReturnRemainingOrders() throws Exception {
        // Arrange
        Long orderId = 1L;
        List<Order> remainingOrders = Arrays.asList(
                new Order("Jane Smith", LocalDate.now(), "456 Elm St", 200.0),
                new Order("Bob Johnson", LocalDate.now(), "789 Oak St", 300.0)
        );
        Mockito.when(orderService.deleteOrder(orderId)).thenReturn(remainingOrders);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/orders/{id}", orderId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerName", is("Jane Smith")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].shippingAddress", is("456 Elm St")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].customerName", is("Bob Johnson")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].shippingAddress", is("789 Oak St")));

        Mockito.verify(orderService, Mockito.times(1)).deleteOrder(orderId);
        Mockito.verifyNoMoreInteractions(orderService);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
