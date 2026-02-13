package com.metropolitan.pz.service;

import com.metropolitan.pz.entities.OrderItem;

import java.util.List;


public interface OrderItemService {

    OrderItem createOrderItem(OrderItem orderItem);

    List<OrderItem> getAllOrderItems();

    OrderItem getOrderItemById(Long id);

    OrderItem updateOrderItem(Long id, OrderItem updatedOrderItem);

    void deleteOrderItem(Long id);
}



