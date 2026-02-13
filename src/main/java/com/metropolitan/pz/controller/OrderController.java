package com.metropolitan.pz.controller;

import com.metropolitan.pz.entities.Order;
import com.metropolitan.pz.entities.enums.OrderStatus;
import com.metropolitan.pz.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ADMIN i MAGACIONER vide sve porudžbine
    @PreAuthorize("hasAnyRole('ADMIN','MAGACIONER')")
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // samo admin/magacioner ili admin
    @PreAuthorize("hasAnyRole('ADMIN','MAGACIONER')")
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // USER pravi porudžbinu
    @PreAuthorize("hasAnyRole('ADMIN','MAGACIONER')")    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    // samo admin
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
}
