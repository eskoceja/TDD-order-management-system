package com.testdrivendevelopment.OrderManagementSystem.controller;

import com.testdrivendevelopment.OrderManagementSystem.model.Order;
import com.testdrivendevelopment.OrderManagementSystem.repository.OrderRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private OrderRepository orderRepository;

    //CREATE
    @Test
    public void createOrderTest() throws Exception {
        Order order = new Order("Shrek", LocalDate.now(), "123 Swamp", 100.0);
        order.setId(1L);

        when(orderRepository.save(Mockito.any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.setId(1L);
            return savedOrder;
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerName\": \"Shrek\", \"shippingAddress\": \"123 Swamp\", \"total\": 100.0}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    //READ
    @Test
    public void getOrderByIdTest() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setCustomerName("Shrek");
        order.setOrderDate(LocalDate.parse("2023-06-11"));
        order.setShippingAddress("123 Swamp");
        order.setTotal(100.0);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName", Matchers.is("Shrek")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderDate", Matchers.is("2023-06-11")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shippingAddress", Matchers.is("123 Swamp")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.is(100.0)));
    }

    @Test
    public void getAllOrdersTest() throws Exception {
        Order order1 = new Order("Shrek", LocalDate.now(), "123 Swamp", 100.0);
        Order order2 = new Order("Donkey", LocalDate.now(), "Swamp Neighbor", 200.0);
        order1.setId(1L);
        order2.setId(2L);
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);

        when(orderRepository.findAll()).thenReturn(orderList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(2))).andDo(print());

    }

    //UPDATE
    @Test
    public void updateOrderTest() throws Exception {
        Order order = new Order("Shrek", LocalDate.now(), "123 Swamp", 100.0);
        order.setId(1L);

        when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(order));
        when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerName\": \"Fiona\", \"shippingAddress\": \"123 Swamp Ave\", \"total\": 200.0}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("Fiona"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shippingAddress").value("123 Swamp Ave"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(200.0));
    }

    //DELETE
    @Test
    public void deleteOrderTest() throws Exception {
        Order order = new Order("Shrek", LocalDate.now(), "123 Swamp", 100.0);
        order.setId(1L);

        when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(order));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/orders/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    //VALIDATION TEST CASES
    @Test
    public void createOrder_ErrorsTest() throws Exception {
        Order order = new Order("", LocalDate.now(), "", -100.0);
        order.setId(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerName\": \"\", \"shippingAddress\": \"\", \"total\": -100.0}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Customer name is required")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Shipping address is required")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Total must be a positive value")));

    }

    @Test
    public void updateOrder_OrderDoesNotExist() throws Exception {
        Order order = new Order("Shrek", LocalDate.now(), "123 Swamp", 100.0);
        order.setId(1L);

        when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerName\": \"Fiona\", \"shippingAddress\": \"123 Swamp Ave\", \"total\": 200.0}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteOrder_OrderDoesNotExist() throws Exception {
        Order order = new Order();
        order.setId(7L);

        when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/orders/7"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
