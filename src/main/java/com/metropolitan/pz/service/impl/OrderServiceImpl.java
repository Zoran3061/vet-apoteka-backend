package com.metropolitan.pz.service.impl;
import com.metropolitan.pz.entities.enums.OrderStatus;
import com.metropolitan.pz.entities.Order;
import com.metropolitan.pz.repository.OrderRepository;
import com.metropolitan.pz.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }
    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }
    @Override
    public Order updateOrder(Long id, Order updatedOrder) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        order.setUserId(updatedOrder.getUserId());
        // orderDate se ne menja nakon kreiranja
        return orderRepository.save(order);
    }
    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
    @Override
    public Long getMaxOrderId() {
        Order order = orderRepository.findTopByOrderByIdDesc();
        return order != null ? order.getId() : 0L;
    }
    @Override
    public Order updateStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setStatus(status);
        return orderRepository.save(order);
    }

}

