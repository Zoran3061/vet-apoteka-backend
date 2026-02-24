package com.metropolitan.pz.service.impl;
import com.metropolitan.pz.entities.enums.OrderStatus;
import com.metropolitan.pz.entities.Order;
import com.metropolitan.pz.entities.OrderItem;
import com.metropolitan.pz.entities.Product;
import com.metropolitan.pz.repository.OrderItemRepository;
import com.metropolitan.pz.repository.ProductRepository;
import com.metropolitan.pz.repository.OrderRepository;
import com.metropolitan.pz.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
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
    public Order updateStatus(Long id, OrderStatus newStatus) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus currentStatus = order.getStatus();

        // === DOZVOLJENI PRELAZI ===
        boolean allowed =
                (currentStatus == OrderStatus.NEW &&
                        (newStatus == OrderStatus.READY || newStatus == OrderStatus.CANCELED))
                        ||
                        (currentStatus == OrderStatus.READY &&
                                (newStatus == OrderStatus.SHIPPED || newStatus == OrderStatus.CANCELED));

        if (!allowed) {
            throw new RuntimeException("Nedozvoljen prelaz statusa: "
                    + currentStatus + " → " + newStatus);
        }

        // Ako prelazi u READY → smanji lager
        if (newStatus == OrderStatus.READY) {

            List<OrderItem> items = orderItemRepository.findByOrderId(id);

            for (OrderItem item : items) {

                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));

                if (product.getStock() < item.getQuantity()) {
                    throw new RuntimeException("Nema dovoljno lagera za proizvod: " + product.getName());
                }

                product.setStock(product.getStock() - item.getQuantity());
                productRepository.save(product);
            }
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

}

