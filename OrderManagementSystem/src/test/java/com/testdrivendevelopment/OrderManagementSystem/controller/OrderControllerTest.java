package com.testdrivendevelopment.OrderManagementSystem.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testdrivendevelopment.OrderManagementSystem.model.Order;
import com.testdrivendevelopment.OrderManagementSystem.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@ExtendWith(MockitoExtension.class)
@WebMvcTest
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    //READ
    @Test
    void getAllOrdersTest() throws Exception {
        Order order1 = new Order("Shrek", LocalDate.now(), "123 Swamp", 100.0);
        Order order2 = new Order("Donkey", LocalDate.now(), "Swamp Neighbor", 200.0);
        order1.setId(1L);
        order2.setId(2L);
        List<Order> orderList = new ArrayList<Order>();
        orderList.add(order1);
        orderList.add(order2);

        when(orderService.getAllOrders()).thenReturn(orderList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2))).andDo(print());

    }
    @Test
    void getOderByIdTest() throws Exception {
        Order order1 = new Order("Shrek", LocalDate.now(), "123 Swamp", 100.0);
        Order order2 = new Order("Donkey", LocalDate.now(), "Swamp Neighbor", 200.0);
        order1.setId(1L);
        order2.setId(2L);
        List<Order> orderList = new ArrayList<Order>();
        orderList.add(order1);
        orderList.add(order2);

        when(orderService.getOrderById(order1.getId())).thenReturn(order1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/1")
                .content(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void createOrderSuccessfullyTest() throws Exception {
        Order order1 = new Order("Shrek", LocalDate.now(), "123 Swamp", 100.0);
        order1.setId(1L);

        when(orderService.createOrder(order1)).thenReturn(order1);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String order1Json = objectMapper.writeValueAsString(order1);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(order1Json));

        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.customerName").value("Shrek"))
                .andExpect(jsonPath("$.shippingAddress").value("123 Swamp"))
                .andExpect(jsonPath("$.total").value(100.0));
    }


}
