package com.metropolitan.pz.controller;

import com.metropolitan.pz.entities.Order;
import com.metropolitan.pz.entities.enums.OrderStatus;
import com.metropolitan.pz.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.metropolitan.pz.dto.OrderItemViewDto;
import com.metropolitan.pz.repository.OrderItemRepository;
import com.metropolitan.pz.repository.ProductRepository;
import com.metropolitan.pz.entities.OrderItem;
import com.metropolitan.pz.entities.Product;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService orderService;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderController(OrderService orderService,
                           OrderItemRepository orderItemRepository,
                           ProductRepository productRepository) {
        this.orderService = orderService;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }
    // ADMIN i MAGACIONER vide sve porudžbine
    @PreAuthorize("hasAnyRole('ADMIN','MAGACIONER')")
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // samo ADMIN/MAGACIONER ili ADMIN
    @PreAuthorize("hasAnyRole('ADMIN','MAGACIONER')")
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // USER pravi porudžbinu
    @PreAuthorize("hasRole('USER')")   @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    // samo ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        return orderService.updateOrder(id, updatedOrder);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MAGACIONER')")    @GetMapping("/maxOrderId")
    public ResponseEntity<Long> getMaxOrderId() {
        return ResponseEntity.ok(orderService.getMaxOrderId());
    }

    // MAGACIONER menja status
    @PreAuthorize("hasAnyRole('ADMIN','MAGACIONER')")    @PutMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return orderService.updateStatus(id, status);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MAGACIONER')")
    @GetMapping("/{id}/items")
    public List<OrderItemViewDto> getOrderItems(@PathVariable Long id) {

        List<OrderItem> items = orderItemRepository.findByOrderId(id);

        return items.stream().map(item -> {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            return new OrderItemViewDto(
                    product.getId(),
                    product.getName(),
                    item.getQuantity()
            );
        }).toList();
    }
}
