package com.metropolitan.pz.service;

import com.metropolitan.pz.entities.Order;
import com.metropolitan.pz.entities.enums.OrderStatus;
import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();

    Order getOrderById(Long id);

    Order createOrder(Order order);

    Order updateOrder(Long id, Order updatedOrder);

    Order updateStatus(Long id, OrderStatus status);

    void deleteOrder(Long id);

    Long getMaxOrderId();
}
