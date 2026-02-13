package com.metropolitan.pz.service.impl;

import com.metropolitan.pz.entities.OrderItem;
import com.metropolitan.pz.entities.enums.OrderStatus;
import com.metropolitan.pz.repository.OrderItemRepository;
import com.metropolitan.pz.repository.OrderRepository;
import com.metropolitan.pz.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }
    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }
    @Override
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }
    @Override
    public OrderItem getOrderItemById(Long id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order item not found with id: " + id));
    }
    @Override
    public OrderItem updateOrderItem(Long id, OrderItem updatedOrderItem) {
        OrderItem existingOrderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order item not found with id: " + id));

        // Update the fields of the existing order item
        existingOrderItem.setOrderId(updatedOrderItem.getOrderId());
        existingOrderItem.setProductId(updatedOrderItem.getProductId());

        return orderItemRepository.save(existingOrderItem);
    }

    @Override
    public void deleteOrderItem(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order item not found with id: " + id));

        orderItemRepository.delete(orderItem);
    }
}

