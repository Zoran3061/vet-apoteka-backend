package com.metropolitan.pz;

import com.metropolitan.pz.controller.OrderController;
import com.metropolitan.pz.entities.Order;
import com.metropolitan.pz.repository.OrderRepository;
import com.metropolitan.pz.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllOrders() {
        List<Order> orders = new ArrayList<>();
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderController.getAllOrders();

        assertEquals(orders, result);
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testGetOrderById() {
        Long orderId = 1L;
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order result = orderController.getOrderById(orderId);

        assertEquals(order, result);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    public void testCreateOrder() {
        Order order = new Order();
        LocalDateTime orderDate = LocalDateTime.now();
        order.setOrderDate(orderDate);
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderController.createOrder(order);

        assertEquals(order, result);
        verify(orderRepository, times(1)).save(order);
    }


    @Test
    public void testDeleteOrder() {
        Long orderId = 1L;
        doNothing().when(orderRepository).deleteById(orderId);

        orderController.deleteOrder(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    public void testGetMaxOrderId() {
        Long maxOrderId = 10L;
        when(orderService.getMaxOrderId()).thenReturn(maxOrderId);

        ResponseEntity<Long> result = orderController.getMaxOrderId();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(maxOrderId, result.getBody());
        verify(orderService, times(1)).getMaxOrderId();
    }
}

