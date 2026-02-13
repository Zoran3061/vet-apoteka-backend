package com.metropolitan.pz.service.impl;

import com.metropolitan.pz.entities.Cart;
import com.metropolitan.pz.entities.Order;
import com.metropolitan.pz.entities.OrderItem;
import com.metropolitan.pz.repository.CartRepository;
import com.metropolitan.pz.repository.OrderItemRepository;
import com.metropolitan.pz.repository.OrderRepository;
import com.metropolitan.pz.service.CartService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           OrderRepository orderRepository,
                           OrderItemRepository orderItemRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public Cart addToCart(Cart cart) {
        return cartRepository.findByUserIdAndProductId(cart.getUserId(), cart.getProductId())
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + cart.getQuantity());
                    return cartRepository.save(existing);
                })
                .orElseGet(() -> cartRepository.save(cart));
    }

    @Override
    public List<Cart> getCartItemsByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public Optional<Cart> getCartItemById(Long cartId) {
        return cartRepository.findById(cartId);
    }

    @Override
    public Cart updateCartItem(Long cartId, Cart updatedCart) {
        Optional<Cart> existingCart = cartRepository.findById(cartId);
        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setQuantity(updatedCart.getQuantity());
            return cartRepository.save(cart);
        }
        throw new RuntimeException("Cart item not found with id: " + cartId);
    }

    @Override
    public void deleteCartItem(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    @Override
    public void deleteCartItemByUserId(Long userId) {
        cartRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public Order checkout(Long userId) {
        List<Cart> items = cartRepository.findByUserId(userId);

        if (items == null || items.isEmpty()) {
            throw new RuntimeException("Cart is empty for userId=" + userId);
        }

        // 1) napravljen order (status/date će postaviti @PrePersist)
        Order order = new Order();
        order.setUserId(userId);
        order = orderRepository.save(order);

        // 2) napravljen orderItems (po količini)
        for (Cart c : items) {
            int qty = c.getQuantity();
            for (int i = 0; i < qty; i++) {
                OrderItem oi = new OrderItem();
                oi.setOrderId(order.getId());
                oi.setProductId(c.getProductId());
                orderItemRepository.save(oi);
            }
        }

        // 3) isprazni cart
        cartRepository.deleteByUserId(userId);

        return order;
    }
}
